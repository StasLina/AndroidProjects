package com.example.recyclerview

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class ColorData(val colorName: String, val colorHex: Int)

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

        // Создаем список с данными о цветах
        val colors = arrayListOf(
            ColorData("White", Color.WHITE),
            ColorData("Red", Color.RED),
            ColorData("Black", Color.BLACK),
            ColorData("Blue", Color.BLUE),
            ColorData("Magenta", Color.MAGENTA)
        )

        // Настраиваем RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(this, colors)
    }
}