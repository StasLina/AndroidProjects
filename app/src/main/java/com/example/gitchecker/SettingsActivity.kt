package com.example.gitchecker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Связываем макет с Activity
        setContentView(R.layout.activity_connection_settings)

        val btnSettingsSave: Button = findViewById(R.id.SaveClick)
        val bExit: ImageView = findViewById(R.id.Close)

        btnSettingsSave.setOnClickListener {
            Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
        }

        bExit.setOnClickListener {
            Toast.makeText(this, "Выходим!", Toast.LENGTH_SHORT).show();
            finish()
        }
    }
}