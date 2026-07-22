package com.lordsandlegends.crew.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.ui.theme.LLColors

/**
this is a simple canvas that tracks the touch of your finger so that it is able to
 track it and draw a line around it
 */
@Composable
fun SignaturePad(
    modifier: Modifier = Modifier,
    onStrokesChanged: (List<List<Offset>>) -> Unit = {},
    clearSignal: Int = 0, // bump this from the parent to trigger a clear
) {
    val strokes = remember { mutableStateListOf<List<Offset>>() }
    var currentStroke by remember { mutableStateOf<List<Offset>>(emptyList()) }

    LaunchedEffect(clearSignal) {
        if (clearSignal > 0) {
            strokes.clear()
            currentStroke = emptyList()
            onStrokesChanged(emptyList())
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(LLColors.Surface, RoundedCornerShape(14.dp))
            .border(1.dp, LLColors.LineStrong, RoundedCornerShape(14.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> currentStroke = listOf(offset) },
                    onDragEnd = {
                        if (currentStroke.size > 1) strokes.add(currentStroke)
                        currentStroke = emptyList()
                        onStrokesChanged(strokes.toList())
                    },
                    onDrag = { change, _ ->
                        currentStroke = currentStroke + change.position
                    },
                )
            },
    ) {
        val allStrokes = strokes + listOf(currentStroke)
        allStrokes.forEach { points ->
            if (points.size < 2) return@forEach
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(path = path, color = LLColors.Ink, style = Stroke(width = 4.5f))
        }
    }
}