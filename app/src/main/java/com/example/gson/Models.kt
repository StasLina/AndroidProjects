package com.example.gson

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
)

data class PhotoPage(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: String,
    val photo: List<Photo>
)

data class Wrapper(
    val photos: PhotoPage
)
