package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.BackBar
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.Pill
import com.lordsandlegends.crew.ui.components.ProgressRing
import com.lordsandlegends.crew.ui.components.SectionHeading
import com.lordsandlegends.crew.ui.components.VideoCardData
import com.lordsandlegends.crew.ui.components.VideoGroup
import com.lordsandlegends.crew.ui.components.VideoSheetState
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

@Composable
fun AcademyScreen(onBack: () -> Unit, onPlayVideo: (VideoSheetState) -> Unit) {
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
            title = "Tutorials",
            onBack = onBack,
            trailingIcon = Icons.Outlined.Search,
            onTrailing = { /* demo */ },
        )

        Hero()

        SectionHeading("Watch & Learn", sub = "Short videos from the head bartender and our floor manager.")

        VideoGroup(
            eyebrow = "Behind the bar",
            headline = "Cocktail technique",
            items = COCKTAIL_VIDEOS,
            onPlay = onPlayVideo,
        )

        Spacer(Modifier.height(18.dp))

        VideoGroup(
            eyebrow = "On the floor",
            headline = "Mastering Pilot",
            items = PILOT_VIDEOS,
            onPlay = onPlayVideo,
        )

        SectionHeading("Modules")

        FilterPills()
        Spacer(Modifier.height(12.dp))

        Modules()
    }
}

@Composable
private fun Hero() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(2.dp).background(LLColors.Copper)
        )
        Column(modifier = Modifier.padding(20.dp)) {
            Eyebrow("Your path")
            Spacer(Modifier.height(2.dp))
            Text("Waiter or Bartender", style = LLType.Headline.copy(fontSize = 24.sp), color = LLColors.Ink)
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProgressRing(percent = 60f)
                Spacer(Modifier.width(18.dp))
                Column {
                    Text("6 of 10 modules complete", style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = LLColors.Ink)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Please complete the following before doing your Test",
                        style = LLType.BodySmall,
                        color = LLColors.Muted,
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterPills() {
    val labels = listOf("All", "Service", "Wine & bar", "Compliance", "Kitchen")
    var active by remember { mutableStateOf(0) }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(end = 4.dp),
    ) {
        items(labels.size) { i ->
            Pill(text = labels[i], active = i == active, onClick = { active = i })
        }
    }
}

@Composable
private fun Modules() {
    val modules = listOf(
        ModuleData("01", "Service · 12 min", "Welcoming the Guest", "The first ninety seconds at the table.", State.Done),
        ModuleData("02", "Service · 18 min", "The Lords & Legends Dress code", "What is to be expected of you.", State.Done),
        ModuleData("03", "Compliance · 9 min", "What Alcohol  do we sell", "All Alcohol we sell ", State.Done),
        ModuleData("04", "Wine & bar · 24 min", " The Wine List", "Learn our selection.", State.Current, progress = 0.35f),
        ModuleData("05", "Service · 15 min", "Drinks list", "How to take, mark and recover an allergen order.", State.Locked),
        ModuleData("06", "Service · 11 min", "The art of upselling", "Recommending without pressuring.", State.Locked),
        ModuleData("07", "Kitchen · 20 min", "The menu, plate by plate", "what food we sell.", State.Locked),
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        modules.forEach { ModuleRow(it) }
    }
}

private enum class State { Done, Current, Locked }

private data class ModuleData(
    val num: String,
    val cat: String,
    val title: String,
    val sub: String,
    val state: State,
    val progress: Float = 0f,
)

@Composable
private fun ModuleRow(m: ModuleData) {
    val bg = if (m.state == State.Current) LLColors.Parchment else LLColors.Surface
    val border = if (m.state == State.Current) LLColors.CopperSoft else LLColors.Line
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(18.dp))
            .border(1.dp, border, RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            m.num,
            style = LLType.Title.copy(fontSize = 22.sp),
            color = LLColors.Steel,
            modifier = Modifier.width(28.dp),
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            val catText = if (m.state == State.Done) "Completed · ${m.cat}" else m.cat
            Eyebrow(catText, color = if (m.state == State.Done) LLColors.Good else LLColors.Muted)
            Spacer(Modifier.height(2.dp))
            Text(m.title, style = LLType.Title.copy(fontSize = 17.sp), color = LLColors.Ink)
            Spacer(Modifier.height(2.dp))
            Text(m.sub, style = LLType.BodySmall, color = LLColors.Muted)

            if (m.state == State.Current) {
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color(0x1A1C2532), RoundedCornerShape(99.dp)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(m.progress)
                            .height(3.dp)
                            .background(LLColors.Copper, RoundedCornerShape(99.dp)),
                    )
                }
            }
        }
        Spacer(Modifier.width(8.dp))
        when (m.state) {
            State.Done -> Icon(
                Icons.Outlined.CheckCircle,
                contentDescription = "Completed",
                tint = LLColors.Good,
                modifier = Modifier.size(22.dp),
            )
            State.Locked -> Icon(
                Icons.Outlined.Lock,
                contentDescription = "Locked",
                tint = LLColors.Steel,
                modifier = Modifier.size(20.dp),
            )
            State.Current -> Box(
                modifier = Modifier
                    .background(LLColors.Copper, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text("Resume", color = Color.White, style = LLType.BodySmall.copy(fontWeight = FontWeight.SemiBold))
            }
        }
    }
}
//here ive set up the youtube play cards u add a youtube video by add the last v=code for example
//https://www.youtube.com/watch?v=6Zy5VLcEbZc the 6Zy5VLcEbZc is the id for the youtube video
private val COCKTAIL_VIDEOS = listOf(
    VideoCardData(
        key = "cocktail-1",
        cat = "cocktail ",
        title = "Blue Logon ",
        desc = "how to make a blue lagon.",
        duration = "4:12",
        gradient = listOf(Color(0xFF2A1A14), Color(0xFF5A2C1D)),
        youtubeId = "APzwFz70XPQ",
    ),
    VideoCardData(
        key = "cocktail-2",
        cat = "strawberry daiquiri",
        title = "strawberry daiquiri ",
        desc = "How to make a strawberry daiquiri",
        duration = "5:48",
        gradient = listOf(Color(0xFF3D2A1A), Color(0xFF8A5A35)),
        youtubeId = "H2QD90xrJRU",
    ),
    VideoCardData(
        key = "cocktail-3",
        cat = "Lords Special",
        title = "Lords Special",
        desc = "How to make a lords special",
        duration = "3:36",
        gradient = listOf(Color(0xFF1A1A26), Color(0xFF44292D)),
        youtubeId = "bdyHcyrXEQ8",
    ),
)

private val PILOT_VIDEOS = listOf(
    VideoCardData(
        key = "pilot-1",
        cat = "Pilot POS · Basics",
        title = "Opening a tab & sending the first order",
        desc = "From sit-down to fired in the kitchen, in under a minute.",
        duration = "6:20",
        gradient = listOf(Color(0xFF1F2A39), Color(0xFF3A4F66)),
        youtubeId = "XvlRG6uBBSY",
    ),
    VideoCardData(
        key = "pilot-2",
        cat = "Pilot POS · Payments",
        title = "Splits, voids and the cash-up",
        desc = "How to handle complicated bills without holding up the table.",
        duration = "4:55",
        gradient = listOf(Color(0xFF25334A), Color(0xFF4A5E75)),
        //this will be the youtube link
        youtubeId = "dQw4w9WgXcQ",
    ),
)
