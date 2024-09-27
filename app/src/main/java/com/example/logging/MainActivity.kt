package com.example.logging

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.Button
import android.widget.EditText
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

        val editText = findViewById<EditText>(R.id.edit_text)
        val buttonLog = findViewById<Button>(R.id.button_log)
        val buttonTimber = findViewById<Button>(R.id.button_timber)

        Timber.plant(Timber.DebugTree());

        // Обработчик для кнопки button_log
        buttonLog.setOnClickListener {
            val text = editText.text.toString()
            Log.v("From EditText", text) // Логирование с использованием Log
        }

        // Обработчик для кнопки button_timber
        buttonTimber.setOnClickListener {
            val text = editText.text.toString()
            Timber.v(text) // Логирование с использованием Timber
        }
    }
}