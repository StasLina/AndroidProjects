package com.example.retrofitforecaster

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitforecaster.Utils.WhetherUtils
import com.example.retrofitforecaster.databinding.ActivityMainBinding
import com.example.retrofitforecaster.models.MainViewModel
import com.example.retrofitforecaster.whether.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    companion object {
        const val DEFAULT_TOWN: String = "Москва"
        const val TOWN_KEY: String = "town"
    }

    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var whetherAPIService:  IWhetherApiService
    private lateinit var whetherUtils : WhetherUtils

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Иницилизируем ViewBinding
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        // Получаем модель
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Включаем Timber для логирования
        Timber.plant(Timber.DebugTree())

        // Иницилизируем api адаптер
        activityBinding.rView.layoutManager = LinearLayoutManager(this)
        whetherAPIService = RetrofitHelper.getInstance().create(IWhetherApiService::class.java)

        // инцилизируем инструменты погоды
        whetherUtils = WhetherUtils(viewModel);

        // Биндим обнволение погоды
        viewModel.getWeatherStore.getWeather.observe(this){ newValue->
                    val adapter = DayListAdapter(whetherUtils)
                    adapter.submitList(newValue?.list)
                    activityBinding.rView.adapter = adapter
        }

        if (savedInstanceState != null) {
            // Обработка сохранённjого состояния
            loadSavedInstanceSet(savedInstanceState);
        } else {
            // Первая загрузка приложения
            firstLoadWhether();
        }

        // Логика смены города
        viewModel.getSelectedTown.observe(this) { newTown ->
            activityBinding.ChooseTown.text= "Выбранный город: $newTown"
            val coroutineExceptionHandler : CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            }
            loadWhetherItems(activityBinding.rView,coroutineExceptionHandler)
        }

        activityBinding.BFindTown.setOnClickListener {
            val newTown = activityBinding.SearchingTown.text.toString();
            viewModel.setSelectedTown(newTown);
        }

        // Логика переключения фаренгейтов
        activityBinding.temperatureSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Фарегнейты
                viewModel.setTypeOfTemperatureScale(IWhetherApiService.IMPERIAL)
            } else {
                viewModel.setTypeOfTemperatureScale(IWhetherApiService.METRIC)
            }
        }

        // При изменении делаем тоже обновление
        viewModel.getTypeOfTemperatureScale.observe(this) { newTypeScale ->
            val coroutineExceptionHandler : CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            }
            loadWhetherItems(activityBinding.rView,coroutineExceptionHandler)
        }
    }

    fun loadSavedInstanceSet(savedInstanceState : Bundle){
        val json = savedInstanceState.getString("weather_data")
        if (json != null) {
            val gson = Gson()
            val weathers = gson.fromJson(json, WeatherForecastResponse::class.java)
            viewModel.getWeatherStore.save(weathers)
            Timber.d("Данные восстановлены из Bundle: $json")
            val adapter = DayListAdapter(whetherUtils)
            adapter.submitList(weathers.list)
            activityBinding.rView.adapter = adapter
        } else {
            Timber.d("Сохранённые данные отсутствуют")
        }
    }

    fun firstLoadWhether() {
        // Загрузка данных из хранилища если есть
        val sharedPreferences: SharedPreferences = getSharedPreferences("WhetherAppPref", MODE_PRIVATE)
        val lastSelectedTown: String? = sharedPreferences.getString(TOWN_KEY, DEFAULT_TOWN)
        if (lastSelectedTown == null) {
            viewModel.setSelectedTown(DEFAULT_TOWN);
        } else {
            viewModel.setSelectedTown(lastSelectedTown);
        }
    }

    // Метод обновления
    fun loadWhetherItems(rView: RecyclerView,
                                coroutineExceptionHandler : CoroutineExceptionHandler) {
        val context = this;
        GlobalScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = whetherAPIService.getWeatherForecastByCityName(
                viewModel.getSelectedTown.value.toString(),
                BuildConfig.API_KEY_OPEN_WEATHER_MAP,
                viewModel.getTypeOfTemperatureScale.value.toString()
            )

            if (response.isSuccessful) {
                val days = response.body()
                days?.let {
                    if (viewModel.getWeatherStore.isEquals(it)) {
                        Timber.d("Data equals")
                        Toast.makeText(context, "Список не изменился", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.getWeatherStore.save(it)
                        Timber.d(viewModel.getWeatherStore.toString())
                    }
                }
            } else {
                // Обработка ошибок
                runOnUiThread {
                    when (response.code()) {
                        400 -> Toast.makeText(context, "Ошибка 400: Неверный запрос", Toast.LENGTH_SHORT).show()
                        401 -> Toast.makeText(context, "Ошибка 401: Неавторизован", Toast.LENGTH_SHORT).show()
                        403 -> Toast.makeText(context, "Ошибка 403: Доступ запрещен", Toast.LENGTH_SHORT).show()
                        404 -> Toast.makeText(context, "Ошибка 404: Город не найден", Toast.LENGTH_SHORT).show()
                        500 -> Toast.makeText(context, "Ошибка 500: Внутренняя ошибка сервера", Toast.LENGTH_SHORT).show()
                        503 -> Toast.makeText(context, "Ошибка 503: Сервис недоступен", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(context, "Ошибка ${response.code()}: Неизвестная ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Проверяем, есть ли данные для сохранения
        val weathers = viewModel.getWeatherStore.getWeather.value
        if (weathers != null) {
            val gson = Gson()
            val json = gson.toJson(weathers)
            outState.putString("weather_data", json) // Сохраняем JSON-строку в Bundle
            Timber.d("Данные сохранены в Bundle: $json")
        } else {
            Timber.d("Данные отсутствуют, сохранение не требуется")
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("WhetherAppPref", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(TOWN_KEY, "Москва")
        editor.apply()
    }

}