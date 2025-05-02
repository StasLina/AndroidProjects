package com.example.navigationbetweenscreens.data

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.example.navigationbetweenscreens.data.local.ImageDao
import com.example.navigationbetweenscreens.data.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    private val contentResolver: ContentResolver,
    private val imageDao: ImageDao
) {

    suspend fun getImages(): List<String> = withContext(Dispatchers.IO) {
        val images = mutableListOf<String>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                images.add(contentUri.toString())
            }
        }

        return@withContext images
    }

    suspend fun getImageDescription(imageUri: String): String? {
        return imageDao.getImageByUri(imageUri)?.description
    }

    suspend fun saveImageDescription(imageUri: String, description: String) {
        val image = Image(uri = imageUri, description = description)
        imageDao.insertImage(image)
    }
}