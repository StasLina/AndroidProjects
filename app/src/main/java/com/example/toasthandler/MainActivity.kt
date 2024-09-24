package com.example.toasthandler

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.toasthandler.ui.theme.ToastHandlerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Найти кнопку по id
        val button = findViewById<Button>(R.id.button_ok)

        // Установить обработчик клика для кнопки
        button.setOnClickListener {
            // Создание и отображение Toast сообщения
            val toast = Toast.makeText(this, "Кнопка ОК", Toast.LENGTH_SHORT)
                toast.show()

            // Убираем Toast через 2 секунды
            android.os.Handler().postDelayed({
                toast.cancel()
            }, 2000) // 2000 миллисекунд = 2 секунды
        }

    }
}