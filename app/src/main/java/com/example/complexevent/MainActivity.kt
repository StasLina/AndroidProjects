package com.example.complexevent

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var buttonSave: Button;


    lateinit var editText : EditText
    lateinit var textView : TextView
    lateinit var progressBar : ProgressBar
    lateinit var checkBox : CheckBox

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

         editText = findViewById(R.id.ETInputText)
         textView = findViewById(R.id.TVResult)
         progressBar = findViewById(R.id.PBProgress)
         checkBox = findViewById(R.id.CBSave)

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
                    Toast.makeText(this, "Прогресс бар уже полный%", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Сообщение, если флажок не установлен
                Toast.makeText(this, "Пожалуйсто нажмите галочку сохранить", Toast.LENGTH_SHORT).show()
            }
        }
    }
}