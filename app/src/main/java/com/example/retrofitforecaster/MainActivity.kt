package com.example.retrofitforecaster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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


        val rView: RecyclerView = findViewById(R.id.r_view)
        rView.layoutManager = LinearLayoutManager(this)
        val daysApi = RetrofitHelper.getInstance().create(DayGetter::class.java)

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        GlobalScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val days = daysApi.check(BuildConfig.API_KEY_OPEN_WEATHER_MAP)

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