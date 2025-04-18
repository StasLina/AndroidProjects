package com.example.canvas.presentation.main.contract

import android.graphics.Bitmap
import com.example.canvas.domain.model.Brush

interface DrawingContract {
    interface View {
        fun updateBrush(brush: Brush)
        fun showImage(image: Bitmap)
        fun showSaveSuccess()
        fun showError(message: String)
    }

    interface Presenter {
        fun onColorSelected(color: Int)
        fun onBrushSizeChanged(size: Float)
        fun onSaveClicked(bitmap: Bitmap)
        fun onLoadClicked()
        fun getCurrentBrush(): Brush
        abstract fun updateBrushColor(color: Int)
    }
}
