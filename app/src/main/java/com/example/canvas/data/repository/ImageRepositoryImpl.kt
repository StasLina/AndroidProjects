package com.example.canvas.data.repository

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.canvas.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor() : ImageRepository {
    override suspend fun saveImage(bitmap: Bitmap, context: Context): Result<Unit> {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "drawing_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return Result.failure(Exception("Failed to create file"))

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            } ?: return Result.failure(Exception("Failed to save image"))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadImage(context: Context): Result<Bitmap?> {
        return try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (context as Activity).startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)

            // В реальном приложении результат будет обработан в onActivityResult
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
    }
}
