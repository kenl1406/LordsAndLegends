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
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lordsandlegends.crew.ui.components.BadgeText
import com.lordsandlegends.crew.ui.components.TopBar
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

@Composable
fun ProfileScreen(onSignOut: () -> Unit) {
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
            title = "Profile",
            trailingIcon = Icons.Outlined.Settings,
            onTrailing = { /* demo */ },
        )

        // avatar block
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .background(LLColors.Surface, CircleShape)
                    .border(1.dp, LLColors.Line, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text("JH", style = LLType.Headline.copy(fontSize = 30.sp), color = LLColors.CopperDeep)
            }
            Spacer(Modifier.height(12.dp))
            Text("James Harper", style = LLType.Headline.copy(fontSize = 26.sp), color = LLColors.Ink)
            Spacer(Modifier.height(4.dp))
            Text("waiter · Terrace section", style = LLType.Body, color = LLColors.Muted)
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

            }
        }

        // settings list
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LLColors.Surface, RoundedCornerShape(18.dp))
                .border(1.dp, LLColors.Line, RoundedCornerShape(18.dp)),
        ) {



            SettingsRow(Icons.Outlined.SupportAgent, "Help & manager chat")
            Divider()
            SettingsRow(
                icon = Icons.Outlined.Logout,
                label = "Sign out",
                tint = LLColors.Bad,
                showChevron = false,
                onClick = onSignOut,
            )
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    tint: androidx.compose.ui.graphics.Color = LLColors.Steel,
    showChevron: Boolean = true,
    onClick: () -> Unit = { /* demo */ },
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(14.dp))
        Text(
            label,
            style = LLType.Body.copy(fontWeight = FontWeight.Medium),
            color = if (tint == LLColors.Bad) LLColors.Bad else LLColors.Ink,
            modifier = Modifier.weight(1f),
        )
        if (showChevron) {
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = LLColors.Muted, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(LLColors.Line),
    )
}
