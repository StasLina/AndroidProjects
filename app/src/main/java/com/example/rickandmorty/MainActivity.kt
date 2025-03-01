package com.example.rickandmorty

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.API.IRickAndMortyApi
import com.example.rickandmorty.API.RetrofitHelper
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.example.rickandmorty.models.MainViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var apiRickAndMorty: IRickAndMortyApi
    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Иницилизируем ViewBinding
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        // Получаем модель
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        apiRickAndMorty = RetrofitHelper.getInstance().create(IRickAndMortyApi::class.java)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        activityBinding.rView.layoutManager = linearLayoutManager
        adapter = CharacterAdapter()
        activityBinding.rView.adapter = adapter

        viewModel.getSelectionData.observe(this) { newValue ->
            runOnUiThread {
                newValue?.results?.let { results ->
                    adapter.submitList(results.toList())
                    Toast.makeText(this, "${results.size}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        val coroutineExceptionHandler: CoroutineExceptionHandler =
            CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            }

        loadRickAndMortyItems(activityBinding.rView, coroutineExceptionHandler)
    }

    fun loadRickAndMortyItems(rView: RecyclerView,
                         coroutineExceptionHandler : CoroutineExceptionHandler
    ) {
        val context = this;
        GlobalScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = apiRickAndMorty.getCharacters()

            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    Timber.d(it.toString())
                    viewModel.setSelectedData(it)
                }
            } else {
                // Обработка ошибок
                runOnUiThread {
                    when (response.code()) {
                        400 -> Toast.makeText(context, "Ошибка 400: Неверный запрос", Toast.LENGTH_SHORT).show()
                        401 -> Toast.makeText(context, "Ошибка 401: Неавторизован", Toast.LENGTH_SHORT).show()
                        403 -> Toast.makeText(context, "Ошибка 403: Доступ запрещен", Toast.LENGTH_SHORT).show()
                        404 -> Toast.makeText(context, "Ошибка 404: Город не найден", Toast.LENGTH_SHORT).show()
                        500 -> Toast.makeText(context, "Ошибка 500: Внутренняя ошибка сервера", Toast.LENGTH_SHORT).show()
                        503 -> Toast.makeText(context, "Ошибка 503: Сервис недоступен", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(context, "Ошибка ${response.code()}: Неизвестная ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}