package com.example.gson

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import timber.log.Timber

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

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    // Метод для получения данных через API
    private fun fetchPhotos() {
        val client = OkHttpClient()
        val url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Timber.e(e, "Request Failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if (response.isSuccessful && !json.isNullOrEmpty()) {
                    parsePhotos(json)
                }
            }
        })
    }

    // Метод для парсинга JSON и логирования каждого пятого объекта
    private fun parsePhotos(json: String) {
        val gson = Gson()
        val wrapper = gson.fromJson(json, Wrapper::class.java)

        wrapper.photos.photo.forEachIndexed { index, photo ->
            if ((index + 1) % 5 == 0) {
                Timber.d("Every 5th photo: $photo")
            }
        }
    }
}