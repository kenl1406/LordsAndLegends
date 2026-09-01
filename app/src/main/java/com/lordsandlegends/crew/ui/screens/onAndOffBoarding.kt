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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.BackBar
import com.lordsandlegends.crew.ui.components.Chip
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.SectionHeading
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

//data

private data class ChecklistPerson(val name: String, val role: String)

private data class ChecklistItem(val label: String, var done: Boolean)

//screen

@Composable
fun OnboardingOffboardingScreen(onBack: () -> Unit) {
    var tab by remember { mutableStateOf(0) } // 0 = onboarding, 1 = offboarding

    val onboardingItems = remember {
        mutableStateListOf(
            ChecklistItem("Sign employment contract", true),
            ChecklistItem("Complete tax & banking forms", true),
            ChecklistItem("Issue uniform & name badge", false),
            ChecklistItem("Grant POS / rota system access", false),
            ChecklistItem("Assign onboarding buddy", false),
            ChecklistItem("Complete Academy tutorials", false),
            ChecklistItem("Health & safety induction", false),
        )
    }

    val offboardingItems = remember {
        mutableStateListOf(
            ChecklistItem("Confirm last working day", true),
            ChecklistItem("Return uniform & name badge", false),
            ChecklistItem("Revoke POS / rota system access", false),
            ChecklistItem("Final pay & outstanding tips reconciled", false),
            ChecklistItem("Exit interview scheduled", false),
            ChecklistItem("Return keys / locker cleared", false),
        )
    }

    val activeItems = if (tab == 0) onboardingItems else offboardingItems
    val activePerson = if (tab == 0) ONBOARDING_PERSON else OFFBOARDING_PERSON
    val doneCount = activeItems.count { it.done }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LLColors.Parchment2)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(bottom = 24.dp),
    ) {
        BackBar(
            title = "Onboarding & Offboarding",
            onBack = onBack,
            trailingIcon = Icons.Outlined.PersonAdd,
            onTrailing = { /* add employee */ },
        )

        TabRow(tab = tab, onSelect = { tab = it })

        Spacer(Modifier.height(14.dp))

        PersonCard(person = activePerson, done = doneCount, total = activeItems.size)

        SectionHeading(
            if (tab == 0) "Onboarding checklist" else "Offboarding checklist",
            sub = if (tab == 0) "Complete before first shift." else "Complete before final pay is released.",
        )

        activeItems.forEachIndexed { index, item ->
            ChecklistRow(
                item = item,
                onToggle = {
                    activeItems[index] = item.copy(done = !item.done)
                },
            )
        }
    }
}

//tabs

@Composable
private fun TabRow(tab: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .background(LLColors.Surface, RoundedCornerShape(99.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(99.dp))
            .padding(4.dp),
    ) {
        listOf("Onboarding", "Offboarding").forEachIndexed { i, label ->
            val isActive = i == tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isActive) LLColors.Ink else Color.Transparent,
                        RoundedCornerShape(99.dp),
                    )
                    .clickable { onSelect(i) }
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

//person summary

@Composable
private fun PersonCard(person: ChecklistPerson, done: Int, total: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(LLColors.Parchment, CircleShape)
                    .border(1.dp, LLColors.Line, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(person.name.take(1), style = LLType.Title.copy(fontSize = 17.sp), color = LLColors.CopperDeep)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(person.name, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = LLColors.Ink)
                Spacer(Modifier.height(2.dp))
                Text(person.role, style = LLType.BodySmall, color = LLColors.Muted)
            }
            Chip(text = "$done of $total done", background = LLColors.Parchment, foreground = LLColors.Ink)
        }
        Spacer(Modifier.height(14.dp))
        ProgressTrack(fraction = if (total == 0) 0f else done.toFloat() / total)
    }
}

@Composable
private fun ProgressTrack(fraction: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(LLColors.Parchment, RoundedCornerShape(99.dp)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .height(6.dp)
                .background(LLColors.Copper, RoundedCornerShape(99.dp)),
        )
    }
}

//checklist rows

@Composable
private fun ChecklistRow(item: ChecklistItem, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(LLColors.Surface, RoundedCornerShape(16.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(16.dp))
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(if (item.done) LLColors.Copper else LLColors.Parchment, CircleShape)
                .border(1.dp, if (item.done) LLColors.Copper else LLColors.Line, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (item.done) {
                Icon(Icons.Outlined.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(Modifier.width(14.dp))
        Text(
            item.label,
            style = LLType.Body.copy(
                textDecoration = if (item.done) TextDecoration.LineThrough else TextDecoration.None,
            ),
            color = if (item.done) LLColors.Muted else LLColors.Ink,
            modifier = Modifier.weight(1f),
        )
    }
}

private val ONBOARDING_PERSON = ChecklistPerson("Zanele Mokoena", "Incoming · Waitstaff")
private val OFFBOARDING_PERSON = ChecklistPerson("Thabo Mahlangu", "Leaving · Bartender")