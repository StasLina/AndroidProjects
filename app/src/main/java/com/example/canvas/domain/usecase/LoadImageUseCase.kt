package com.example.canvas.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import com.example.canvas.domain.repository.ImageRepository
import javax.inject.Inject

class LoadImageUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(context: Context): Result<Bitmap?> {
        return repository.loadImage(context)
    }
}
