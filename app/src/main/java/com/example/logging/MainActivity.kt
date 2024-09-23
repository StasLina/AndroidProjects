package com.example.logging

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

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