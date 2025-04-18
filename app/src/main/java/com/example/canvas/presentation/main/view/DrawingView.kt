package com.example.canvas.presentation.main.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.canvas.domain.model.Brush
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var path = Path()
    private val brush = Brush()
    private val paths = mutableListOf<Pair<Path, Brush>>()
    private var currentBitmap: Bitmap? = null

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = brush.color
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = brush.size
    }

    fun setBrush(brush: Brush) {
        this.brush.color = brush.color
        this.brush.size = brush.size
        paint.color = brush.color
        paint.strokeWidth = brush.size
    }

    fun setImage(bitmap: Bitmap) {
        currentBitmap = bitmap
        invalidate()
    }

    fun clear() {
        paths.clear()
        currentBitmap = null
        path = Path()
        invalidate()
    }

    fun getDrawingBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        currentBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, null, Rect(0, 0, width, height), null)
        }

        paths.forEach { (path, brush) ->
            paint.color = brush.color
            paint.strokeWidth = brush.size
            canvas.drawPath(path, paint)
        }

        paint.color = brush.color
        paint.strokeWidth = brush.size
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path = Path()
                path.moveTo(x, y)
                paths.add(path to brush.copy())
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
            }
            else -> return false
        }
        invalidate()
        return true
    }
}