package com.example.attributes

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editText = findViewById(R.id.editText)

        // Обработчики кнопок
        findViewById<Button>(R.id.buttonBlack).setOnClickListener {
            editText.setTextColor(Color.BLACK)
        }

        findViewById<Button>(R.id.buttonRed).setOnClickListener {
            editText.setTextColor(Color.RED)
        }

        findViewById<Button>(R.id.buttonSize85).setOnClickListener {
            editText.textSize = 85f
        }

        findViewById<Button>(R.id.buttonSize24).setOnClickListener {
            editText.textSize = 24f
        }

        findViewById<Button>(R.id.buttonWhiteBg).setOnClickListener {
            editText.setBackgroundColor(Color.WHITE)
        }

        findViewById<Button>(R.id.buttonYellowBg).setOnClickListener {
            editText.setBackgroundColor(Color.YELLOW)
        }
    }
}