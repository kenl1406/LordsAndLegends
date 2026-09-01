package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.BackBar
import com.lordsandlegends.crew.ui.components.Chip
import com.lordsandlegends.crew.ui.components.CopperButton
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.GhostButton
import com.lordsandlegends.crew.ui.components.Pill
import com.lordsandlegends.crew.ui.components.SectionHeading
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

//data

private enum class LeaveStatus { PENDING, APPROVED, DECLINED }

private data class LeaveRequest(
    val name: String,
    val type: String,
    val dates: String,
    val days: String,
    val status: LeaveStatus,
)

//screen

@Composable
fun LeaveManagementScreen(onBack: () -> Unit) {
    var showForm by remember { mutableStateOf(false) }

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
            title = "Leave Management",
            onBack = onBack,
            trailingIcon = Icons.Outlined.Add,
            onTrailing = { showForm = !showForm },
        )

        BalanceGrid()

        Spacer(Modifier.height(14.dp))

        if (showForm) {
            RequestLeaveCard(onCancel = { showForm = false }, onSubmit = { showForm = false })
            Spacer(Modifier.height(4.dp))
        } else {
            CopperButton(text = "Request leave", onClick = { showForm = true })
        }

        SectionHeading("Team requests", sub = "Pending approvals appear first.")

        LEAVE_REQUESTS.forEach { request ->
            LeaveRequestRow(request)
        }
    }
}

//balances

@Composable
private fun BalanceGrid() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BalanceCard(label = "Annual leave", value = "12", suffix = "days left", modifier = Modifier.weight(1f))
        BalanceCard(label = "Sick leave", value = "6", suffix = "days left", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BalanceCard(label: String, value: String, suffix: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(16.dp),
    ) {
        Eyebrow(label, color = LLColors.Muted)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, style = LLType.Headline.copy(fontSize = 30.sp), color = LLColors.Ink)
            Spacer(Modifier.width(6.dp))
            Text(suffix, style = LLType.BodySmall, color = LLColors.Muted, modifier = Modifier.padding(bottom = 4.dp))
        }
    }
}

//request form

@Composable
private fun RequestLeaveCard(onCancel: () -> Unit, onSubmit: () -> Unit) {
    var selectedType by remember { mutableStateOf(0) }
    val types = listOf("Annual", "Sick", "Unpaid")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(20.dp),
    ) {
        Text("New leave request", style = LLType.Title, color = LLColors.Ink)
        Spacer(Modifier.height(14.dp))

        Eyebrow("Leave type", color = LLColors.Muted)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            types.forEachIndexed { i, label ->
                Pill(text = label, active = i == selectedType, onClick = { selectedType = i })
            }
        }

        Spacer(Modifier.height(16.dp))

        Eyebrow("Dates", color = LLColors.Muted)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LLColors.Parchment, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = LLColors.CopperDeep, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Text("Select start and end date", style = LLType.Body, color = LLColors.Muted)
        }

        Spacer(Modifier.height(18.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            GhostButton(text = "Cancel", onClick = onCancel, modifier = Modifier.weight(1f))
            CopperButton(text = "Submit", onClick = onSubmit, modifier = Modifier.weight(1f))
        }
    }
}

//requests list

@Composable
private fun LeaveRequestRow(request: LeaveRequest) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(LLColors.Parchment, CircleShape)
                .border(1.dp, LLColors.Line, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                request.name.take(1),
                style = LLType.Title.copy(fontSize = 16.sp),
                color = LLColors.CopperDeep,
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(request.name, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = LLColors.Ink)
            Spacer(Modifier.height(2.dp))
            Text("${request.type} · ${request.dates} · ${request.days}", style = LLType.BodySmall, color = LLColors.Muted)
        }
        Spacer(Modifier.width(10.dp))
        StatusChip(request.status)
    }
}

@Composable
private fun StatusChip(status: LeaveStatus) {
    val (label, bg, fg) = when (status) {
        LeaveStatus.PENDING -> Triple("Pending", LLColors.Parchment, LLColors.Ink)
        LeaveStatus.APPROVED -> Triple("Approved", LLColors.Good.copy(alpha = 0.14f), LLColors.Good)
        LeaveStatus.DECLINED -> Triple("Declined", LLColors.Bad.copy(alpha = 0.12f), LLColors.Bad)
    }
    Chip(text = label, background = bg, foreground = fg)
}

private val LEAVE_REQUESTS = listOf(
    LeaveRequest("Naledi Khumalo", "Sick", "12 Sep", "1 day", LeaveStatus.PENDING),
    LeaveRequest("Sipho Ndlovu", "Annual", "20 – 24 Sep", "5 days", LeaveStatus.PENDING),
    LeaveRequest("James Okoro", "Annual", "2 – 3 Sep", "2 days", LeaveStatus.APPROVED),
    LeaveRequest("Priya Reddy", "Unpaid", "15 Aug", "1 day", LeaveStatus.DECLINED),
)