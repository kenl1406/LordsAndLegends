package com.lordsandlegends.crew.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

data class PieSegment(val percent: Float, val color: Color, val label: String, val value: String)

@Composable
fun Donut(
    segments: List<PieSegment>,
    diameter: androidx.compose.ui.unit.Dp = 124.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 22.dp,
) {
    Canvas(modifier = Modifier.size(diameter)) {
        val stroke = Stroke(strokeWidth.toPx())
        val pad = stroke.width / 2f
        val rect = androidx.compose.ui.geometry.Rect(
            offset = Offset(pad, pad),
            size = Size(size.width - pad * 2, size.height - pad * 2),
        )

        var start = -90f
        segments.forEach { seg ->
            val sweep = seg.percent * 3.6f
            drawArc(
                color = seg.color,
                startAngle = start,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = stroke,
            )
            start += sweep
        }
    }
}

@Composable
fun DonutWithLegend(segments: List<PieSegment>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Donut(segments = segments)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            segments.forEach { LegendRow(it) }
        }
    }
}

@Composable
private fun LegendRow(seg: PieSegment) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(seg.color, RoundedCornerShape(3.dp)),
        )
        Spacer(Modifier.width(8.dp))
        Text(seg.label, style = LLType.BodySmall, color = LLColors.Muted, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(8.dp))
        Text(seg.value, style = LLType.BodySmall.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold), color = LLColors.Ink)
    }
}

@Composable
fun ProgressRing(
    percent: Float,
    diameter: androidx.compose.ui.unit.Dp = 80.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 6.dp,
    progressColor: Color = LLColors.Copper,
    trackColor: Color = Color(0xFFECE2CD),
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(diameter)) {
        Canvas(modifier = Modifier.size(diameter)) {
            val stroke = Stroke(strokeWidth.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            val pad = stroke.width / 2f
            val rect = androidx.compose.ui.geometry.Rect(
                offset = Offset(pad, pad),
                size = Size(size.width - pad * 2, size.height - pad * 2),
            )
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(strokeWidth.toPx()),
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = percent.coerceIn(0f, 100f) * 3.6f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = stroke,
            )
        }
        Text(
            "${percent.toInt()}%",
            style = LLType.Body.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
            color = LLColors.Ink,
        )
    }
}
