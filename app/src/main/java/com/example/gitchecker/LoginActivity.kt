package com.example.gitchecker
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Связываем макет с Activity
        setContentView(R.layout.activity_login)

        val btnLogin: Button = findViewById(R.id.LoginClick)
        val btnSettings: ImageView = findViewById(R.id.ConnectionSettings)
        val bExit: ImageView = findViewById(R.id.Close)

        btnLogin.setOnClickListener{
            Toast.makeText(this, "Логин или пароль не верны!", Toast.LENGTH_SHORT).show();
        }

        btnSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
//            Toast.makeText(this, "Логин или пароль не верны!", Toast.LENGTH_SHORT).show();
        }

        bExit.setOnClickListener{
            Toast.makeText(this, "Выходим!", Toast.LENGTH_SHORT).show();
            finish()
        }
    }
}