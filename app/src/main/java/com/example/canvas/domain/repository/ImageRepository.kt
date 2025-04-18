package com.example.canvas.domain.repository

import android.content.Context
import android.graphics.Bitmap

interface ImageRepository {
    suspend fun saveImage(bitmap: Bitmap, context: Context): Result<Unit>
    suspend fun loadImage(context: Context): Result<Bitmap?>
}