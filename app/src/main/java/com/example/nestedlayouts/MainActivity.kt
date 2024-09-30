package com.example.nestedlayouts

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var currentValue = 1
    private lateinit var textViews: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Получаем ссылки на все TextView, которые будут обновляться
        textViews = listOf(
            findViewById(R.id.textViewLinearHorizontal1),
            findViewById(R.id.textViewLinearHorizontal2),
            findViewById(R.id.textViewLinearHorizontal3),
            findViewById(R.id.textViewLinearVertical1),
            findViewById(R.id.textViewLinearVertical2),
            findViewById(R.id.textViewLinearVertical3),
            findViewById(R.id.textViewConstraint1),
            findViewById(R.id.textViewConstraint2),
            findViewById(R.id.textViewConstraint3)
        )

        // Обработчик нажатия кнопки
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            incrementTextView()
        }
    }

    // Метод для увеличения значения в TextView
    private fun incrementTextView() {
        // Найдем следующий TextView, который нужно обновить
        for (textView in textViews) {
            if (textView.text.isEmpty()) {
                textView.text = (++currentValue).toString()
                return
            }
        }
    }
}