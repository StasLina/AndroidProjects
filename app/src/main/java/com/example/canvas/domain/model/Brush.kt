package com.example.canvas.domain.model

import android.graphics.Color

data class Brush(
    var color: Int = Color.BLACK,
    var size: Float = 10f
)