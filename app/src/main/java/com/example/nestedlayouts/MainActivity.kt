package com.example.nestedlayouts

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var currentValue = 0

    //    private lateinit var textViews: List<TextView>
    private var textViews = Array(3) { arrayOfNulls<TextView>(3) }

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
        textViews[0][0] = findViewById(R.id.textViewLinearHorizontal1);
        textViews[0][1] = findViewById(R.id.textViewLinearHorizontal2);
        textViews[0][2] = findViewById(R.id.textViewLinearHorizontal3);
        textViews[1][0] = findViewById(R.id.textViewLinearVertical1);
        textViews[1][1] = findViewById(R.id.textViewLinearVertical2);
        textViews[1][2] = findViewById(R.id.textViewLinearVertical3);
        textViews[2][0] = findViewById(R.id.textViewConstraint1);
        textViews[2][1] = findViewById(R.id.textViewConstraint2);
        textViews[2][2] = findViewById(R.id.textViewConstraint3);

        // Обработчик нажатия кнопки
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            incrementTextView()
        }
    }

    private fun incrementTextView() {
        val index = currentValue % 3

        for (i1 in 0..2) {
            val isEqual: Boolean = i1 == index
            if (isEqual) {
                for (i2 in 0..2) {
                    textViews[i1][i2]?.text = "${index+1}"
                }
            } else {
                for (i2 in 0..2) {
                    textViews[i1][i2]?.text = " "
                }
            }
        }
        ++currentValue;
    }
}