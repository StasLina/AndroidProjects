package com.example.complexevent

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var buttonSave: Button;


    lateinit var editText = findViewById(R.id.editText)
    lateinit var textView = findViewById(R.id.textView)
    lateinit var progressBar = findViewById(R.id.progressBar)
    lateinit var checkBox = findViewById(R.id.checkBox)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonSave = findViewById<Button>(R.id.BtSave);


        editText = findViewById(R.id.editText)
        textView = findViewById(R.id.textView)
        progressBar = findViewById(R.id.progressBar)
        checkBox = findViewById(R.id.checkBox)

        // Обработчик нажатия на кнопку
        buttonSave.setOnClickListener {
            // Проверка, установлен ли флажок
            if (checkBox.isChecked) {
                // Сохраняем текст из EditText в TextView
                val inputText = editText.text.toString()
                textView.text = inputText

                // Увеличиваем прогресс на 10%
                val currentProgress = progressBar.progress
                if (currentProgress < 100) {
                    progressBar.progress = currentProgress + 10
                } else {
                    Toast.makeText(this, "Progress is already at 100%", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Сообщение, если флажок не установлен
                Toast.makeText(this, "Please check the box to save text", Toast.LENGTH_SHORT).show()
            }
        }
    }
}