package com.example.canvas.presentation.main.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvas.data.repository.ImageRepositoryImpl
import com.example.canvas.databinding.ActivityMainBinding
import com.example.canvas.domain.model.Brush
import com.example.canvas.presentation.main.DrawingViewModel
import com.example.canvas.presentation.main.contract.DrawingContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DrawingContract.View {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DrawingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.attachView(this)

        binding.brushSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onBrushSizeChanged(progress.toFloat())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        viewModel.onColorSelected(Color.GREEN)
        setupColorPalette()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
    }

    override fun updateBrush(brush: Brush) {
        binding.drawingView.setBrush(brush)
    }

    override fun showImage(image: Bitmap) {
        binding.drawingView.setImage(image)
    }

    override fun showSaveSuccess() {
        Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageRepositoryImpl.REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    showImage(bitmap)
                } catch (e: Exception) {
                    showError("Failed to load image")
                }
            }
        }
    }

    private fun setupColorPalette() {
        val colors = listOf(
            Color.BLACK, Color.RED, Color.BLUE,
            Color.GREEN, Color.YELLOW, Color.WHITE
        )

        // Инициализация RecyclerView
        binding.rvColors.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Получаем начальное значение цвета (предполагая, что brushColor - StateFlow<Int>)
        val initialColor = viewModel.getCurrentBrush().color ?: Color.BLACK

        val adapter = ColorAdapter(
            colors = colors,
            selectedColor = initialColor,
            onColorSelected = { color ->
//                viewModel.updateBrushColor(color)
                viewModel.onColorSelected(color)
            }
        )

        binding.rvColors.adapter = adapter

        // Подписываемся на изменения цвета (для StateFlow)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentBrushState.collect { newColor ->
                    adapter.updateSelected(newColor.color)
                }
            }
        }
    }
}