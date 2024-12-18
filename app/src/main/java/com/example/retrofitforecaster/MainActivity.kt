package com.example.retrofitforecaster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

interface IMemento{
    fun isEquals(otherInstance: DataResponse) : Boolean
    fun save(otherInstance: DataResponse)
    fun get() : DataResponse
}
val WeatherStore = object : IMemento{
    var weathers : DataResponse? = null;

    override  fun get() : DataResponse{
        return weathers!!;
    }
    override fun isEquals(otherInstance: DataResponse) : Boolean{
        if(weathers == null) return false;
        return weathers == otherInstance;
    }
    // тип анонимных объектов - Any, поэтому `override` необходим в `toString()`
    override fun toString() : String {
        if(weathers == null) return "Данные не установлены"
        val gson = Gson()
        return gson.toJson(weathers)
    }

    override fun save(otherInstance: DataResponse) {
        weathers = otherInstance;
    }
};

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Включаем Timber для логирования
        Timber.plant(Timber.DebugTree())

        val rView: RecyclerView = findViewById(R.id.r_view)
        rView.layoutManager = LinearLayoutManager(this)
        val daysApi = RetrofitHelper.getInstance().create(DayGetter::class.java)

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        if (savedInstanceState != null) {
            val json = savedInstanceState.getString("weather_data")
            if (json != null) {
                val gson = Gson()
                val weathers = gson.fromJson(json, DataResponse::class.java)
                WeatherStore.save(weathers)
                Timber.d("Данные восстановлены из Bundle: $json")

                val adapter = DayListAdapter()
                adapter.submitList(weathers.list)
                rView.adapter = adapter
            } else {
                Timber.d("Сохранённые данные отсутствуют")
            }
        } else {
            GlobalScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                val days = daysApi.check(BuildConfig.API_KEY_OPEN_WEATHER_MAP)
                var d = days.body();
                d?.let {
                    if (WeatherStore.isEquals(it)) {
                        Timber.d("Data equals")
                    } else {
                        WeatherStore.save(it)
                        Timber.d(WeatherStore.toString())
                    }
                }

                withContext(Dispatchers.Main) {
                    if (days.body() != null) {
                        val adapter = DayListAdapter()
                        adapter.submitList(days.body()?.list)
                        rView.adapter = adapter
                    }
                }
            }
        }

    }

    object RetrofitHelper {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Проверяем, есть ли данные для сохранения
        val weathers = WeatherStore.get()
        if (weathers != null) {
            val gson = Gson()
            val json = gson.toJson(weathers)
            outState.putString("weather_data", json) // Сохраняем JSON-строку в Bundle
            Timber.d("Данные сохранены в Bundle: $json")
        } else {
            Timber.d("Данные отсутствуют, сохранение не требуется")
        }
    }

}