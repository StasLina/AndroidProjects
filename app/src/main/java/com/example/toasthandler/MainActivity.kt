package com.example.toasthandler

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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