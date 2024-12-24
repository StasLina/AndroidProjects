package com.example.gitchecker

import android.R.attr.password
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64


class MainActivity : AppCompatActivity() {

    fun ByteArray.toBase64(): String =
        String(Base64.getEncoder().encode(this))


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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun dobby(){
        // Запускаем сетевое взаимодействие в другом потоке
        Thread {
            try {
                // Указываем URL для запроса
//              val url = URL("http://192.192.56.111:3000/api/v1/users/Stanislav/tokens")
                val url = URL("http://172.28.96.2:3000/api/v1/users/Stanislav/tokens")

                // Открываем соединение
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                // Устанавливаем Basic Authentication
                val username = "Stanislav"
                val password = "i2Ekbi9p.JYegiz"
                val auth = "$username:$password".toByteArray().toBase64() // Кодируем в Base64
                urlConnection.setRequestProperty("Authorization", "Basic $auth")

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
}