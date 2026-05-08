package com.lordsandlegends.crew.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

@Composable
fun BottomTabBar(current: Screen, onSelect: (Screen) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Parchment2)
            .border(1.dp, LLColors.Line),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Tab("Overview", Icons.Outlined.Home, current == Screen.Overview) { onSelect(Screen.Overview) }
            Tab("Academy", Icons.Outlined.School, current == Screen.Academy) { onSelect(Screen.Academy) }
            Tab("Performance", Icons.Outlined.BarChart, current == Screen.Performance) { onSelect(Screen.Performance) }
            Tab("Profile", Icons.Outlined.Person, current == Screen.Profile) { onSelect(Screen.Profile) }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.Tab(
    label: String,
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (active) LLColors.Ink else LLColors.Muted
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // active indicator
        Box(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .height(3.dp)
                .size(width = 24.dp, height = 3.dp)
                .background(
                    if (active) LLColors.Copper else androidx.compose.ui.graphics.Color.Transparent,
                    RoundedCornerShape(99.dp),
                )
        )
        Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(3.dp))
        Text(
            label,
            color = tint,
            style = LLType.BodySmall.copy(fontSize = androidx.compose.ui.unit.TextUnit.Unspecified),
        )
    }
}
