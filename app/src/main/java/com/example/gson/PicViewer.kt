package com.example.gson

import android.app.Activity
import android.content.Intent
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

    lateinit var photoData: Photo

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

        photoData = intent.getParcelableExtra("data")!!


        supportActionBar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Timber.v("OnCreateOptions")
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Обработка нажатий на картинку
        val id = item.getItemId()

        if (id == R.id.action_one) {

            item.icon = ContextCompat.getDrawable(this, R.drawable.star_svgrepo_com)
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_LONG).show()

            val resultIntent = Intent()

            // Передаем ссылку на картинку и информацию о добавлении в избранное
            resultIntent.putExtra("imageUrl", photoData.getURI())  // Замените на реальный URL изображения
            resultIntent.putExtra("isFavorite", true)  // Устанавливаем, что картинка добавлена в избранное

            // Устанавливаем результат и завершаем Activity
            setResult(Activity.RESULT_OK, resultIntent)
            finish()  // Закрываем текущую Activity
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}