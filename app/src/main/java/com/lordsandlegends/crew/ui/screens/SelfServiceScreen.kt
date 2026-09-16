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
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ReceiptLong
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.BackBar
import com.lordsandlegends.crew.ui.components.Chip
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.Pill
import com.lordsandlegends.crew.ui.components.SectionHeading
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

/* ------------------------------------------------------------------
   UI models — to be filled from Supabase later. No sample data here.
   ------------------------------------------------------------------ */

data class EmployeeProfileUi(
    val fullName: String,
    val jobTitle: String,
    val employeeNumber: String,
    val email: String,
    val phone: String,
)

data class LeaveBalanceUi(val label: String, val daysLeft: Int, val daysTotal: Int)

data class PayslipUi(val id: String, val month: String, val period: String, val netPay: String)

enum class SelfServiceRequestStatus { PENDING, APPROVED, DECLINED }

data class SelfServiceRequestUi(
    val id: String,
    val employeeName: String,
    val type: String,       // e.g. "Annual leave", "Profile update"
    val detail: String,     // e.g. dates
    val status: SelfServiceRequestStatus,
)

/* ------------------------------------------------------------------ */

@Composable
fun SelfServiceScreen(
    onBack: () -> Unit,
    profile: EmployeeProfileUi? = null,
    leaveBalances: List<LeaveBalanceUi> = emptyList(),
    payslips: List<PayslipUi> = emptyList(),
    myRequests: List<SelfServiceRequestUi> = emptyList(),
    showManagerTab: Boolean = true,                 // later: only for managers / higher roles
    teamRequests: List<SelfServiceRequestUi> = emptyList(),
    onPayslipClick: (PayslipUi) -> Unit = {},
    onApprove: (SelfServiceRequestUi) -> Unit = {},
    onDecline: (SelfServiceRequestUi) -> Unit = {},
) {
    var tab by remember { mutableStateOf(0) } // 0 = My portal, 1 = Manager

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LLColors.Parchment2)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(bottom = 24.dp),
    ) {
        BackBar(title = "Self-Service", onBack = onBack)

        if (showManagerTab) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill(text = "My portal", active = tab == 0, onClick = { tab = 0 })
                Pill(text = "Manager approvals", active = tab == 1, onClick = { tab = 1 })
            }
            Spacer(Modifier.height(14.dp))
        }

        if (tab == 0 || !showManagerTab) {
            EmployeePortal(profile, leaveBalances, payslips, myRequests, onPayslipClick)
        } else {
            ManagerApprovals(teamRequests, onApprove, onDecline)
        }
    }
}

/* ---------------- employee view ---------------- */

@Composable
private fun EmployeePortal(
    profile: EmployeeProfileUi?,
    leaveBalances: List<LeaveBalanceUi>,
    payslips: List<PayslipUi>,
    myRequests: List<SelfServiceRequestUi>,
    onPayslipClick: (PayslipUi) -> Unit,
) {
    ProfileHeaderCard(profile)

    SectionHeading("Leave balance")
    if (leaveBalances.isEmpty()) {
        EmptyCard(Icons.Outlined.EventNote, "No leave balance yet", "Your leave days will show here.")
    } else {
        leaveBalances.chunked(3).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                row.forEach { BalanceTile(it, Modifier.weight(1f)) }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }

    SectionHeading("Payslips", sub = "Tap a payslip to view or download it.")
    if (payslips.isEmpty()) {
        EmptyCard(Icons.Outlined.ReceiptLong, "No payslips yet", "Payslips will appear here once payroll is run.")
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LLColors.Surface, RoundedCornerShape(18.dp))
                .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp),
        ) {
            payslips.forEachIndexed { i, slip ->
                PayslipRow(slip, latest = i == 0, onClick = { onPayslipClick(slip) })
                if (i != payslips.lastIndex) {
                    Box(Modifier.fillMaxWidth().height(1.dp).background(LLColors.Line))
                }
            }
        }
    }

    SectionHeading("My requests")
    if (myRequests.isEmpty()) {
        EmptyCard(Icons.Outlined.Inbox, "No requests", "Requests you submit will show here with their status.")
    } else {
        myRequests.forEach { RequestRow(it, showActions = false) }
    }
}

@Composable
private fun ProfileHeaderCard(profile: EmployeeProfileUi?) {
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
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(LLColors.Navy, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    val initials = profile?.fullName
                        ?.split(" ")?.filter { it.isNotBlank() }?.take(2)
                        ?.joinToString("") { it.take(1).uppercase() }
                    if (initials.isNullOrEmpty()) {
                        Icon(Icons.Outlined.Person, null, tint = LLColors.Bone, modifier = Modifier.size(24.dp))
                    } else {
                        Text(initials, style = LLType.Title, color = LLColors.Bone)
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(profile?.fullName ?: "—", style = LLType.Headline, color = LLColors.Ink)
                    Text(profile?.jobTitle ?: "—", style = LLType.BodySmall, color = LLColors.Muted)
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(LLColors.Line))
            Spacer(Modifier.height(12.dp))
            InfoLine(Icons.Outlined.Badge, "Employee no.", profile?.employeeNumber)
            InfoLine(Icons.Outlined.Email, "Email", profile?.email)
            InfoLine(Icons.Outlined.Phone, "Phone", profile?.phone)
        }
    }
}

@Composable
private fun InfoLine(icon: ImageVector, label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = LLColors.CopperDeep, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, style = LLType.BodySmall, color = LLColors.Muted, modifier = Modifier.width(92.dp))
        Text(value ?: "—", style = LLType.BodySmall.copy(fontWeight = FontWeight.Medium), color = LLColors.Ink)
    }
}

@Composable
private fun BalanceTile(balance: LeaveBalanceUi, modifier: Modifier = Modifier) {
    val fraction = if (balance.daysTotal > 0) balance.daysLeft.toFloat() / balance.daysTotal else 0f
    Column(
        modifier = modifier
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(14.dp),
    ) {
        Eyebrow(balance.label, color = LLColors.Muted)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text("${balance.daysLeft}", style = LLType.Headline.copy(fontSize = 26.sp), color = LLColors.Ink)
            Text(" / ${balance.daysTotal}", style = LLType.BodySmall, color = LLColors.Muted, modifier = Modifier.padding(bottom = 4.dp))
        }
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(LLColors.Parchment, RoundedCornerShape(99.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction.coerceIn(0f, 1f))
                    .height(4.dp)
                    .background(LLColors.Copper, RoundedCornerShape(99.dp))
            )
        }
    }
}

@Composable
private fun PayslipRow(slip: PayslipUi, latest: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(if (latest) LLColors.Copper else LLColors.Parchment, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.ReceiptLong, null, tint = if (latest) Color.White else LLColors.CopperDeep, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(slip.month, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = LLColors.Ink)
            Text(slip.period, style = LLType.BodySmall, color = LLColors.Muted)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(slip.netPay, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = LLColors.Ink)
            Text("Net pay", style = LLType.BodySmall, color = LLColors.Muted)
        }
        Spacer(Modifier.width(10.dp))
        Icon(Icons.Outlined.FileDownload, "Download", tint = LLColors.CopperDeep, modifier = Modifier.size(20.dp))
    }
}

/* ---------------- manager view (UI only) ---------------- */

@Composable
private fun ManagerApprovals(
    requests: List<SelfServiceRequestUi>,
    onApprove: (SelfServiceRequestUi) -> Unit,
    onDecline: (SelfServiceRequestUi) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CountCard("Pending", requests.count { it.status == SelfServiceRequestStatus.PENDING }, Modifier.weight(1f), highlight = true)
        CountCard("Approved", requests.count { it.status == SelfServiceRequestStatus.APPROVED }, Modifier.weight(1f))
        CountCard("Declined", requests.count { it.status == SelfServiceRequestStatus.DECLINED }, Modifier.weight(1f))
    }

    SectionHeading("Requests to review", sub = "Requests from your team appear here.")

    if (requests.isEmpty()) {
        EmptyCard(Icons.Outlined.Inbox, "Nothing to review", "New requests from your team will show here.")
    } else {
        requests.sortedBy { it.status != SelfServiceRequestStatus.PENDING }.forEach { req ->
            RequestRow(req, showActions = true, onApprove = { onApprove(req) }, onDecline = { onDecline(req) })
        }
    }
}

@Composable
private fun CountCard(label: String, value: Int, modifier: Modifier = Modifier, highlight: Boolean = false) {
    Column(
        modifier = modifier
            .background(if (highlight) LLColors.Ink else LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, if (highlight) LLColors.Ink else LLColors.Line, RoundedCornerShape(18.dp))
            .padding(14.dp),
    ) {
        Eyebrow(label, color = if (highlight) LLColors.CopperSoft else LLColors.Muted)
        Spacer(Modifier.height(6.dp))
        Text("$value", style = LLType.Headline.copy(fontSize = 26.sp), color = if (highlight) LLColors.Bone else LLColors.Ink)
    }
}

/* ---------------- shared ---------------- */

@Composable
private fun RequestRow(
    request: SelfServiceRequestUi,
    showActions: Boolean,
    onApprove: () -> Unit = {},
    onDecline: () -> Unit = {},
) {
    val actionable = showActions && request.status == SelfServiceRequestStatus.PENDING
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(LLColors.Surface, RoundedCornerShape(18.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp))
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LLColors.Parchment, CircleShape)
                    .border(1.dp, LLColors.Line, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(request.employeeName.take(1).uppercase(), style = LLType.Title.copy(fontSize = 16.sp), color = LLColors.CopperDeep)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow(request.type, color = LLColors.CopperDeep)
                Spacer(Modifier.height(2.dp))
                Text(request.employeeName, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = LLColors.Ink)
                Text(request.detail, style = LLType.BodySmall, color = LLColors.Muted)
            }
            if (!actionable) {
                Spacer(Modifier.width(10.dp))
                RequestStatusChip(request.status)
            }
        }

        if (actionable) {
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ActionButton("Decline", Icons.Outlined.Close, LLColors.Surface, LLColors.Bad, LLColors.LineStrong, onDecline, Modifier.weight(1f))
                ActionButton("Approve", Icons.Outlined.Check, LLColors.Copper, Color.White, LLColors.Copper, onApprove, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector,
    bg: Color,
    fg: Color,
    border: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(bg, RoundedCornerShape(12.dp))
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = fg, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, style = LLType.Body.copy(fontWeight = FontWeight.SemiBold), color = fg)
    }
}

@Composable
private fun RequestStatusChip(status: SelfServiceRequestStatus) {
    val (label, bg, fg) = when (status) {
        SelfServiceRequestStatus.PENDING -> Triple("Pending", LLColors.Parchment, LLColors.Ink)
        SelfServiceRequestStatus.APPROVED -> Triple("Approved", LLColors.Good.copy(alpha = 0.14f), LLColors.Good)
        SelfServiceRequestStatus.DECLINED -> Triple("Declined", LLColors.Bad.copy(alpha = 0.12f), LLColors.Bad)
    }
    Chip(text = label, background = bg, foreground = fg)
}
