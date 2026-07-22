package com.lordsandlegends.crew.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset

fun strokesToSignatureBitmap(
    strokes: List<List<Offset>>,
    width: Int,
    height: Int,

    /*
    * the signature pad only knows about coordinates. what this does is take those coordinates and turns
    * it into a solid line
    * */
): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // transparent bg
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 6f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }
    strokes.forEach { points ->
        for (i in 0 until points.size - 1) {
            canvas.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y, paint)
        }
    }
    return bitmap
}