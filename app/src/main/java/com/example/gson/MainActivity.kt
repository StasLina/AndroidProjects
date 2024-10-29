package com.example.gson

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity(), ActionOnResultSuccess {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree()) // Инициализация Timber

        Timber.v("Начало инициализации")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Timber.v("Search RecycleView")
        recyclerView = findViewById(R.id.rView)
        // Иницилизируем адаптер
        val adapter = Adapter()
        recyclerView.adapter = adapter
        Timber.v("Set adapter")
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val apiInstance = API()
        apiInstance.fetchPhotos(this)
    }

    override fun ActionOnResultSuccess(eventData: String) {
        val gson = Gson()
        val wrapper = gson.fromJson(eventData, Wrapper::class.java)

        runOnUiThread {
            val adapter = (recyclerView.adapter as Adapter)
            adapter.setPhotos(wrapper.photos.photo)
        }
    }
}
