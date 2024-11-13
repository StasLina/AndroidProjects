package com.example.gson

import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import timber.log.Timber


class PicViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.pic_viewer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras = intent.extras
        val b = extras!!.getByteArray("picture")

        val bmp = BitmapFactory.decodeByteArray(b, 0, b!!.size)
        val image = findViewById<View>(R.id.FavoriteIcon) as ImageView
        image.setImageBitmap(bmp)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        this.setSupportActionBar(toolbar)

        supportActionBar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Timber.v("OnCreateOptions")
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Обработка нажатий кнопок
        val id = item.getItemId()

        if (id == R.id.action_one) {

            item.icon = ContextCompat.getDrawable(this, R.drawable.star_svgrepo_com)
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_LONG).show()
            return true
        }



        return super.onOptionsItemSelected(item)
    }
}