package com.example.canvas.presentation.main

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canvas.domain.model.Brush
import com.example.canvas.domain.usecase.LoadImageUseCase
import com.example.canvas.domain.usecase.SaveImageUseCase
import com.example.canvas.presentation.main.contract.DrawingContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val loadImageUseCase: LoadImageUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel(), DrawingContract.Presenter {
    private var _view: DrawingContract.View? = null
    private val _currentBrush = MutableStateFlow(Brush())

    public val currentBrushState: StateFlow<Brush> get() = _currentBrush

    override fun onColorSelected(color: Int) {
        _currentBrush.update { it.copy(color = color) }
        _view?.updateBrush(_currentBrush.value)
    }


    override fun onBrushSizeChanged(size: Float) {
        _currentBrush.update { it.copy(size = size) }
        _view?.updateBrush(_currentBrush.value)
    }

    override fun onSaveClicked(bitmap: Bitmap) {
        viewModelScope.launch {
            val context = (_view as? Context) ?: return@launch
            saveImageUseCase(bitmap, context).onSuccess {
                _view?.showSaveSuccess()
            }.onFailure {
                _view?.showError(it.message ?: "Failed to save image")
            }
        }
    }

    override fun onLoadClicked() {
        viewModelScope.launch {
            val context = (_view as? Context) ?: return@launch
            loadImageUseCase(context).onSuccess { bitmap ->
                bitmap?.let { _view?.showImage(it) }
            }.onFailure {
                _view?.showError(it.message ?: "Failed to load image")
            }
        }
    }

    override fun getCurrentBrush()  = _currentBrush.asStateFlow().value

    override fun updateBrushColor(color: Int) {
            _currentBrush.value.color = color
    }

    fun attachView(view: DrawingContract.View) {
        _view = view
    }

    fun detachView() {
        _view = null
    }
}