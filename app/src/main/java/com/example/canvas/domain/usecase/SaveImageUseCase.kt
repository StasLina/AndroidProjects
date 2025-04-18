package com.example.canvas.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import com.example.canvas.domain.repository.ImageRepository
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(bitmap: Bitmap, context: Context): Result<Unit> {
        return repository.saveImage(bitmap, context)
    }
}