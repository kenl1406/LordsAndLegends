package com.lordsandlegends.crew.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.FreeBreakfast
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.BackBar
import com.lordsandlegends.crew.ui.components.Chip
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.SectionHeading
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/* ------------------------------------------------------------------
   UI models — to be filled from Supabase later. No sample data here.
   ------------------------------------------------------------------ */

/** Every state the employee's clock can be in. Approval is done by a manager (not built yet). */
enum class ClockStatus {
    OFF_SHIFT,          // nothing requested
    CLOCK_IN_PENDING,   // employee tapped "Start shift", waiting for manager
    CLOCK_IN_DECLINED,  // manager declined the clock-in request
    ON_SHIFT,           // clock-in accepted, shift has started
    BREAK_PENDING,      // employee requested a break, waiting for manager
    BREAK_DECLINED,     // manager declined the break (still on shift)
    ON_BREAK,           // break accepted
}

/** One scheduled shift, e.g. dayName = "Monday", dayOfMonth = "22", month = "September", start = "10:00", end = "20:00". */
data class ShiftUi(
    val dayName: String,
    val dayOfMonth: String,
    val month: String,
    val startTime: String,
    val endTime: String,
    val role: String? = null,
)

/* ------------------------------------------------------------------ */

@Composable
fun TimeAttendanceScreen(
    onBack: () -> Unit,
    status: ClockStatus = ClockStatus.OFF_SHIFT,
    todayShift: ShiftUi? = null,
    upcomingShifts: List<ShiftUi> = emptyList(),
    statusMessage: String? = null,          // optional note from manager (e.g. decline reason)
    onStartShift: () -> Unit = {},
    onCancelRequest: () -> Unit = {},
    onRequestBreak: () -> Unit = {},
    onEndBreak: () -> Unit = {},
    onEndShift: () -> Unit = {},
) {
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1000)
        }
    }
    val timeFmt = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFmt = remember { SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LLColors.Parchment2)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(bottom = 24.dp),
    ) {
        BackBar(title = "Time & Attendance", onBack = onBack)

        // ---------- clock card ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LLColors.Surface, RoundedCornerShape(18.dp))
                .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp)),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(LLColors.Copper)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Eyebrow(dateFmt.format(Date(now)))
                    StatusChip(status)
                }

                Spacer(Modifier.height(16.dp))
                Text(timeFmt.format(Date(now)), style = LLType.Display.copy(fontSize = 52.sp), color = LLColors.Ink)

                Spacer(Modifier.height(6.dp))
                Text(
                    text = todayShift?.let { "Today's shift · ${it.startTime} – ${it.endTime}" } ?: "No shift scheduled today",
                    style = LLType.BodySmall,
                    color = LLColors.Muted,
                )

                Spacer(Modifier.height(22.dp))

                MainActionButton(
                    status = status,
                    onStartShift = onStartShift,
                    onEndShift = onEndShift,
                )

                Spacer(Modifier.height(18.dp))

                StatusBanner(status = status, message = statusMessage)

                // secondary actions
                when (status) {
                    ClockStatus.CLOCK_IN_PENDING, ClockStatus.BREAK_PENDING -> {
                        Spacer(Modifier.height(10.dp))
                        SecondaryAction("Cancel request", Icons.Outlined.Close, onCancelRequest)
                    }
                    ClockStatus.ON_SHIFT, ClockStatus.BREAK_DECLINED -> {
                        Spacer(Modifier.height(10.dp))
                        SecondaryAction("Request break", Icons.Outlined.FreeBreakfast, onRequestBreak)
                    }
                    ClockStatus.ON_BREAK -> {
                        Spacer(Modifier.height(10.dp))
                        SecondaryAction("End break", Icons.Outlined.FreeBreakfast, onEndBreak, dark = true)
                    }
                    else -> Unit
                }
            }
        }

        // ---------- shifts ----------
        SectionHeading("My shifts", sub = "Your upcoming shifts and times.")

        if (upcomingShifts.isEmpty()) {
            EmptyCard(Icons.Outlined.EventBusy, "No upcoming shifts", "When a manager schedules you, your shifts will show here.")
        } else {
            upcomingShifts.forEach { ShiftRow(it) }
        }
    }
}

/* ---------------- components ---------------- */

@Composable
private fun MainActionButton(status: ClockStatus, onStartShift: () -> Unit, onEndShift: () -> Unit) {
    val onShift = status == ClockStatus.ON_SHIFT || status == ClockStatus.ON_BREAK ||
        status == ClockStatus.BREAK_PENDING || status == ClockStatus.BREAK_DECLINED
    val pending = status == ClockStatus.CLOCK_IN_PENDING

    val bg by animateColorAsState(
        when {
            pending -> LLColors.SteelSoft
            onShift -> LLColors.Navy
            else -> LLColors.Copper
        },
        label = "actionBg",
    )
    val label = when {
        pending -> "Waiting…"
        onShift -> "End shift"
        status == ClockStatus.CLOCK_IN_DECLINED -> "Request again"
        else -> "Start shift"
    }
    val icon = when {
        pending -> Icons.Outlined.HourglassTop
        onShift -> Icons.Outlined.Stop
        else -> Icons.Outlined.PlayArrow
    }

    Box(
        modifier = Modifier
            .size(164.dp)
            .background(LLColors.Parchment, CircleShape)
            .border(1.dp, LLColors.Line, CircleShape)
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg, CircleShape)
                .let {
                    if (pending) it else it.clickable(onClick = if (onShift) onEndShift else onStartShift)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(30.dp))
            Spacer(Modifier.height(6.dp))
            Text(label, style = LLType.Title, color = Color.White, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun StatusBanner(status: ClockStatus, message: String?) {
    data class B(val icon: ImageVector, val title: String, val body: String, val bg: Color, val fg: Color)

    val b = when (status) {
        ClockStatus.OFF_SHIFT -> B(
            Icons.Outlined.PlayArrow, "Ready to start",
            "Tap Start shift to send a clock-in request to your manager.",
            LLColors.Parchment, LLColors.Ink,
        )
        ClockStatus.CLOCK_IN_PENDING -> B(
            Icons.Outlined.HourglassTop, "Clock-in request sent",
            "Waiting for a manager to accept your request.",
            LLColors.Parchment, LLColors.Ink,
        )
        ClockStatus.CLOCK_IN_DECLINED -> B(
            Icons.Outlined.Close, "Clock-in request declined",
            "Your shift has not started. Speak to your manager or request again.",
            LLColors.Bad.copy(alpha = 0.10f), LLColors.Bad,
        )
        ClockStatus.ON_SHIFT -> B(
            Icons.Outlined.CheckCircle, "Clock-in accepted",
            "Your shift has started.",
            LLColors.Good.copy(alpha = 0.12f), LLColors.Good,
        )
        ClockStatus.BREAK_PENDING -> B(
            Icons.Outlined.HourglassTop, "Break request sent",
            "Waiting for a manager to accept your break.",
            LLColors.Parchment, LLColors.Ink,
        )
        ClockStatus.BREAK_DECLINED -> B(
            Icons.Outlined.Close, "Break request declined",
            "You are still on shift.",
            LLColors.Bad.copy(alpha = 0.10f), LLColors.Bad,
        )
        ClockStatus.ON_BREAK -> B(
            Icons.Outlined.CheckCircle, "Break accepted",
            "Your break has started. Tap End break when you're back.",
            LLColors.CopperSoft.copy(alpha = 0.6f), LLColors.CopperDeep,
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(b.bg, RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(b.icon, null, tint = b.fg, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(b.title, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = b.fg)
            Spacer(Modifier.height(2.dp))
            Text(message ?: b.body, style = LLType.BodySmall, color = LLColors.Ink.copy(alpha = 0.75f))
        }
    }
}

@Composable
private fun StatusChip(status: ClockStatus) {
    val (label, bg, fg) = when (status) {
        ClockStatus.OFF_SHIFT -> Triple("Off shift", LLColors.Parchment, LLColors.Ink)
        ClockStatus.CLOCK_IN_PENDING, ClockStatus.BREAK_PENDING -> Triple("Pending", LLColors.Parchment, LLColors.CopperDeep)
        ClockStatus.CLOCK_IN_DECLINED -> Triple("Declined", LLColors.Bad.copy(alpha = 0.12f), LLColors.Bad)
        ClockStatus.ON_SHIFT, ClockStatus.BREAK_DECLINED -> Triple("On shift", LLColors.Good.copy(alpha = 0.14f), LLColors.Good)
        ClockStatus.ON_BREAK -> Triple("On break", LLColors.CopperSoft, LLColors.CopperDeep)
    }
    Chip(text = label, background = bg, foreground = fg)
}

@Composable
private fun SecondaryAction(text: String, icon: ImageVector, onClick: () -> Unit, dark: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (dark) LLColors.Ink else LLColors.Surface, RoundedCornerShape(12.dp))
            .border(1.dp, if (dark) LLColors.Ink else LLColors.LineStrong, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = if (dark) LLColors.CopperSoft else LLColors.CopperDeep, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = if (dark) LLColors.Bone else LLColors.Ink)
    }
}

@Composable
private fun ShiftRow(shift: ShiftUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .size(width = 56.dp, height = 60.dp)
                .background(LLColors.Parchment, RoundedCornerShape(14.dp))
                .border(1.dp, LLColors.Line, RoundedCornerShape(14.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(shift.month.take(3).uppercase(), style = LLType.Eyebrow.copy(fontSize = 9.sp), color = LLColors.CopperDeep)
            Text(shift.dayOfMonth, style = LLType.Title.copy(fontSize = 20.sp), color = LLColors.Ink)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "${shift.dayName}, ${shift.dayOfMonth} ${shift.month}",
                style = LLType.Body.copy(fontWeight = FontWeight.SemiBold),
                color = LLColors.Ink,
            )
            Spacer(Modifier.height(2.dp))
            Text("${shift.startTime} – ${shift.endTime}", style = LLType.BodySmall, color = LLColors.Muted)
        }
        shift.role?.let { Chip(text = it, background = LLColors.Parchment, foreground = LLColors.Ink) }
    }
}

@Composable
internal fun EmptyCard(icon: ImageVector, title: String, body: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(LLColors.Parchment, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, tint = LLColors.Muted, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = LLColors.Ink)
            Text(body, style = LLType.BodySmall, color = LLColors.Muted)
        }
    }
}
