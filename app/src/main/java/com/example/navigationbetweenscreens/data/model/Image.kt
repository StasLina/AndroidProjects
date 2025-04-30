package com.example.navigationbetweenscreens.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey val uri: String,
    val description: String
)