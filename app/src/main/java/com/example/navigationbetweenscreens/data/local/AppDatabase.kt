package com.example.navigationbetweenscreens.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.navigationbetweenscreens.data.model.Image

@Database(
    entities = [Image::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}