package com.example.navigationbetweenscreens.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.navigationbetweenscreens.data.model.Image

@Dao
interface ImageDao {

    @Query("SELECT * FROM images WHERE uri = :uri")
    suspend fun getImageByUri(uri: String): Image?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)
}