package com.example.gitchecker

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gitchecker.models.AppData

class ProfileActivity : AppCompatActivity() {
    private val appData: AppData by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//
//        }


        appData.data.userData?.let {
            it.loginName.let {
                findViewById<TextView>(R.id.valueLoginName).text = it
            }

            it.login.let {
                findViewById<TextView>(R.id.valueLogin).text = it
            }

            it.description.let {
                findViewById<TextView>(R.id.valueDescription).text = it
            }

            it.followersCount.let {
                findViewById<TextView>(R.id.valueFollowingCount).text = it.toString()
            }
        }
    }
}