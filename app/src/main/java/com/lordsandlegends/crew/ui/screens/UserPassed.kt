package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.ui.components.SurfaceCard
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

@Composable
fun UserPased() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(LLColors.Parchment2, LLColors.Parchment))
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.padding(top = 64.dp, start = 24.dp, end = 24.dp)) {

            // First Box: Registration Info
            SurfaceCard {
                Column {
                    Text(
                        text = "Congratulations! You passed the Test",
                        style = LLType.Title,
                        color = LLColors.Ink
                    )

                    Text(
                        text = "You are now an employee at Lords and Legends.",
                        style = LLType.Body,
                        color = LLColors.Muted,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Space between the boxes
            Spacer(modifier = Modifier.height(16.dp))

            // Second Box: Contract Info (Bigger)
            SurfaceCard(modifier = Modifier.height(240.dp)) { 
                Column {
                    Text(
                        text = "Fill out your Contract",
                        style = LLType.Title,
                        color = LLColors.Ink
                    )

                    Text(
                        text = "Please complete the details below to finalize your onboarding process.",
                        style = LLType.Body,
                        color = LLColors.Muted,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
