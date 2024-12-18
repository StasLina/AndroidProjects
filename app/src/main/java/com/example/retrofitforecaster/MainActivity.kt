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
}
val memento = object : IMemento{
    var lastData : DataResponse? = null;


    override fun isEquals(otherInstance: DataResponse) : Boolean{
        if(lastData == null) return false;
        return lastData == otherInstance;
    }
    // тип анонимных объектов - Any, поэтому `override` необходим в `toString()`
    override fun toString() : String {
        if(lastData == null) return "Данные не установлены"
        val gson = Gson()
        return gson.toJson(lastData)
    }

    override fun save(otherInstance: DataResponse) {
        lastData = otherInstance;
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

        GlobalScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val days = daysApi.check(BuildConfig.API_KEY_OPEN_WEATHER_MAP)
            var d = days.body();
            d?.let {
                if (memento.isEquals(it)) {
                        Timber.d("Данные совпадают")
                }
                else {
                    memento.save(it)
                    Timber.d(memento.toString())
                }
            }

            Timber.d(memento.toString())
            withContext(Dispatchers.Main) {
                if (days.body() != null) {
                    val adapter = DayListAdapter()
                    adapter.submitList(days.body()?.list)
                    rView.adapter = adapter
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

}