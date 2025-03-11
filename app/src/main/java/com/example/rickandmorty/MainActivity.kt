package com.example.rickandmorty


import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.example.rickandmorty.models.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var adapter: CharacterAdapter

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        adapter = CharacterAdapter()
        activityBinding.rView.layoutManager = LinearLayoutManager(this)
        activityBinding.rView.adapter = adapter

        viewModel.getSelectionData.observe(this) { newValue ->
            newValue?.results?.let { results ->
                adapter.submitList(results.toList())
                Toast.makeText(this, "${results.size}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getErrorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        loadData()
    }

    private fun loadData() {
        viewModel.loadRickAndMortyItems()
    }
}