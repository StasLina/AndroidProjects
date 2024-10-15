package com.example.internettest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.Button
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import okhttp3.*
import java.io.IOException

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

        // Находим кнопку
        val btnHTTP: Button = findViewById(R.id.btnHTTP)

        // Устанавливаем слушатель клика на кнопку
        btnHTTP.setOnClickListener {
            // Запускаем сетевое взаимодействие в другом потоке
            Thread {
                try {
                    // Указываем URL для запроса
                    val url = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")

                    // Открываем соединение
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"

                    // Получаем код ответа
                    val responseCode = urlConnection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Читаем поток данных
                        val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val response = StringBuilder()
                        var line: String?

                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        reader.close()

                        // Логируем ответ
                        Log.d("Flickr cats", "Response: $response")
                    } else {
                        Log.d("Flickr cats", "Error: $responseCode")
                    }

                    urlConnection.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("Flickr cats", "Exception: ${e.message}")
                }
            }.start()
        }

        // Находим кнопку для OkHTTP
        val btnOkHTTP: Button = findViewById(R.id.btnOkHTTP)

        // Устанавливаем слушатель клика для кнопки "Get via OkHTTP"
        btnOkHTTP.setOnClickListener {
            // Запускаем асинхронный запрос через OkHTTP
            fetchPhotosUsingOkHTTP()
        }
    }

    // Функция для выполнения асинхронного запроса через OkHTTP
    private fun fetchPhotosUsingOkHTTP() {
        // Создаем клиент OkHTTP
        val client = OkHttpClient()

        // Указываем URL для запроса
        val url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1"

        // Формируем запрос
        val request = Request.Builder()
            .url(url)
            .build()

        // Асинхронный вызов
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Логируем ошибку в случае неудачного запроса
                Log.e("Flickr OkCats", "Request Failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Получаем тело ответа
                    val responseBody = response.body?.string()
                    // Логируем ответ на уровне INFO
                    Log.i("Flickr OkCats", "Response: $responseBody")
                } else {
                    // Логируем код ошибки
                    Log.e("Flickr OkCats", "Error: ${response.code}")
                }
            }
        })
    }
}