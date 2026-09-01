package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.Chip
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.SectionHeading
import com.lordsandlegends.crew.ui.components.TopBar
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Warning

@Composable
fun OverviewScreen(onAcademy: () -> Unit, onPerformance: () -> Unit, onContracts: () -> Unit,  onWarnings: () -> Unit,) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LLColors.Parchment2)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(bottom = 16.dp),
    ) {
        TopBar(
            greetingTop = "Good evening,",
            greetingBottom = "James",
            trailingIcon = Icons.Outlined.Notifications,
            onTrailing = { /* demo */ },
            trailingDot = true,
        )

        HeroCard()

        Spacer(Modifier.height(8.dp))
        SectionHeading("Overview")

        Tile(
            eyebrow = "",
            title = "Tutorials",
            copy = "Service standards, wine list, and the Lords & Legends way. Complete your training before your first shift.",
            cta = "Start learning",
            artIcon = Icons.Outlined.School,
            artBg = LLColors.Parchment,
            artFg = LLColors.CopperDeep,
            onClick = onAcademy,
        )

        Tile(
            eyebrow = "",
            title = "My Performance",
            copy = "Track your covers, sales and tips — week by week.",
            cta = "View dashboard",
            artIcon = Icons.Outlined.TrendingUp,
            artBg = LLColors.Copper,
            artFg = androidx.compose.ui.graphics.Color.White,
            onClick = onPerformance,
            mini = listOf("This week" to "R 18,240" ),
        )
        Tile(
            eyebrow = "",
            title = "Contracts",
            copy = "Upload contracts for staff to review and sign on their phone.",
            cta = "Manage contracts",
            artIcon = Icons.Outlined.Description,
            artBg = LLColors.Navy,
            artFg = androidx.compose.ui.graphics.Color.White,
            onClick = onContracts,
        )

        Tile(
            eyebrow = "",
            title = "Warnings & Performance",
            copy = "Manage staff warnings and performance notes.",
            cta = "View records",
            artIcon = Icons.Outlined.Warning,  // You may want a different icon
            artBg = LLColors.Bad,
            artFg = androidx.compose.ui.graphics.Color.White,
            onClick = onWarnings,
        )

        SectionHeading("Managers notes for the day")
        NewsRow()
    }
}

@Composable
private fun HeroCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(LLColors.Copper)
        )
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Eyebrow("saturday")
                Text("10 till 8pm", style = LLType.BodySmall, color = LLColors.Muted)
            }
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Draught special", style = LLType.Headline, color = LLColors.Ink)
                    Spacer(Modifier.height(2.dp))
                    Text("All draught are 30 rand", style = LLType.BodySmall, color = LLColors.Muted)
                }

            }
            Spacer(Modifier.height(18.dp))
            Spacer(Modifier.height(8.dp))

        }
    }
}

@Composable
private fun ProgressBar(fraction: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(LLColors.Parchment, RoundedCornerShape(99.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .height(3.dp)
                .background(LLColors.Copper, RoundedCornerShape(99.dp))
        )
    }
}

@Composable
private fun Tile(
    eyebrow: String,
    title: String,
    copy: String,
    cta: String,
    artIcon: ImageVector,
    artBg: androidx.compose.ui.graphics.Color,
    artFg: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    mini: List<Pair<String, String>>? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(20.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(artBg, RoundedCornerShape(14.dp))
                .border(1.dp, LLColors.Line, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(artIcon, contentDescription = null, tint = artFg, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Eyebrow(eyebrow, color = LLColors.Muted)
            Spacer(Modifier.height(4.dp))
            Text(title, style = LLType.Headline.copy(fontSize = 24.sp), color = LLColors.Ink)
            Spacer(Modifier.height(6.dp))
            Text(copy, style = LLType.BodySmall, color = LLColors.Muted)
            if (mini != null) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
                    mini.forEach { (label, value) ->
                        Column {
                            Eyebrow(label, color = LLColors.Muted)
                            Spacer(Modifier.height(2.dp))
                            Text(value, style = LLType.Title.copy(fontSize = 20.sp), color = LLColors.Ink)
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "$cta  →",
                style = LLType.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = LLColors.CopperDeep,
            )
        }
    }
}

@Composable
private fun NewsRow() {
    val items = listOf(
        Triple("Kitchen", "prepare platter", "make 3 platters for the 5th of may."),
        Triple("Bar", "HANG THE MOP WHEN CLOSING", "Outside by the bin area"),
        Triple("Front", "Private booking", "table 34 to 36 10 people"),
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(end = 4.dp),
    ) {
        items(items.size) { i ->
            val (tag, title, body) = items[i]
            Column(
                modifier = Modifier
                    .width(232.dp)
                    .background(LLColors.Surface, RoundedCornerShape(18.dp))
                    .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Eyebrow(tag, color = LLColors.CopperDeep)
                Spacer(Modifier.height(8.dp))
                Text(title, style = LLType.Title.copy(fontSize = 17.sp), color = LLColors.Ink)
                Spacer(Modifier.height(4.dp))
                Text(body, style = LLType.BodySmall, color = LLColors.Muted)
            }
        }
    }
}
