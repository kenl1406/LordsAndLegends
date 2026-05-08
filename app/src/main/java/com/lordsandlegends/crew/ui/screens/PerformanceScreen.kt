package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.BackBar
import com.lordsandlegends.crew.ui.components.Chip
import com.lordsandlegends.crew.ui.components.DonutWithLegend
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.PieSegment
import com.lordsandlegends.crew.ui.components.SectionHeading
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

@Composable
fun PerformanceScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LLColors.Parchment2)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(bottom = 16.dp),
    ) {
        BackBar(
            title = "My Performance",
            onBack = onBack,
            trailingIcon = Icons.Outlined.Tune,
            onTrailing = { /* demo */ },
        )

        PeriodRow()

        Spacer(Modifier.height(14.dp))

        KpiGrid()

        Spacer(Modifier.height(14.dp))

        ChartCard(eyebrow = "Weekly sales", title = "R 18,240", titleSuffix = " this week", chip = "Floor avg R 14,910") {
            DonutWithLegend(WEEKLY_SEGMENTS)
        }

        SectionHeading("Most sold items", sub = "By revenue, this week, your tables.")

        ChartCard {
            DonutWithLegend(ITEM_SEGMENTS)
        }

        Spacer(Modifier.height(14.dp))

    }
}

@Composable
private fun PeriodRow() {
    val labels = listOf("Today", "This week", "Month", )
    var active by remember { mutableIntStateOf(1) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Surface, RoundedCornerShape(99.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(99.dp))
            .padding(4.dp),
    ) {
        labels.forEachIndexed { i, label ->
            val isActive = i == active
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isActive) LLColors.Ink else androidx.compose.ui.graphics.Color.Transparent,
                        RoundedCornerShape(99.dp),
                    )
                    .clickable { active = i }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    color = if (isActive) LLColors.Bone else LLColors.Muted,
                    style = LLType.BodySmall.copy(fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium),
                )
            }
        }
    }
}

@Composable
private fun KpiGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            Kpi("Total sales", "R 18,240", "+ 12.4% vs last week", positive = true, modifier = Modifier.weight(1f))

        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            Kpi("Tips", "R 2,180", "+ 8.1%", positive = true, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun Kpi(label: String, value: String, delta: String, positive: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Eyebrow(label, color = LLColors.Muted)
        Spacer(Modifier.height(6.dp))
        Text(value, style = LLType.Headline.copy(fontSize = 26.sp), color = LLColors.Ink)
        Spacer(Modifier.height(6.dp))
        Text(
            delta,
            color = if (positive) LLColors.Good else LLColors.Bad,
            style = LLType.BodySmall.copy(fontWeight = FontWeight.Medium),
        )
    }
}

@Composable
private fun ChartCard(
    eyebrow: String? = null,
    title: String? = null,
    titleSuffix: String? = null,
    chip: String? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(18.dp),
    ) {
        if (eyebrow != null || title != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column {
                    if (eyebrow != null) Eyebrow(eyebrow, color = LLColors.Muted)
                    if (title != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            buildAnnotatedString {
                                append(title)
                                if (titleSuffix != null) {
                                    withStyle(SpanStyle(fontSize = 12.sp, color = LLColors.Muted, fontWeight = FontWeight.Normal)) {
                                        append(titleSuffix)
                                    }
                                }
                            },
                            style = LLType.Headline.copy(fontSize = 28.sp),
                            color = LLColors.Ink,
                        )
                    }
                }
                if (chip != null) {
                    Chip(text = chip, background = LLColors.Parchment, foreground = LLColors.Ink)
                }
            }
            Spacer(Modifier.height(18.dp))
        }
        content()
    }
}




private val WEEKLY_SEGMENTS = listOf(
    PieSegment(23f, LLColors.Copper, "Friday", "R 4,210"),
    PieSegment(20f, LLColors.Navy, "Saturday", "R 3,640"),
    PieSegment(18f, LLColors.Steel, "Thursday", "R 3,280"),
    PieSegment(23f, LLColors.CopperSoft, "Tue / Wed", "R 4,180"),
    PieSegment(16f, LLColors.SteelSoft, "Mon / Sun", "R 2,930"),
)

private val ITEM_SEGMENTS = listOf(
    PieSegment(34f, LLColors.Copper, "Pork shank", "34%"),
    PieSegment(24f, LLColors.Navy, "Lamb curry", "24%"),
    PieSegment(17f, LLColors.Steel, "monday special", "17%"),
    PieSegment(13f, LLColors.CopperSoft, "Chicken fingers", "13%"),
    PieSegment(12f, LLColors.SteelSoft, "Other", "12%"),
)
