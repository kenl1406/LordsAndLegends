package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.lordsandlegends.crew.R
import com.lordsandlegends.crew.ui.components.CopperButton
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.GhostButton
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

@Composable
fun LoginScreen(onSignIn: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(LLColors.Parchment2, LLColors.Parchment))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 28.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // logo (vector fallback bundled; user can drop logo.png to override)
            Image(
                painter = painterResource(id = R.drawable.lords),
                contentDescription = "Lords and Legends",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(220.dp),
            )

            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(LLColors.Surface, RoundedCornerShape(22.dp))
                    .border(1.dp, LLColors.Line, RoundedCornerShape(22.dp))
                    .padding(24.dp),
            ) {
                Text("Welcome back", style = LLType.Display.copy(fontSize = androidx.compose.ui.unit.TextUnit.Unspecified), color = LLColors.Ink)
                Spacer(Modifier.height(4.dp))
                Text("Sign in to your crew account", style = LLType.Body, color = LLColors.Muted)

                Spacer(Modifier.height(20.dp))

                LabeledField("Employee email", initial = "j.harper@lordsandlegends.co", keyboard = KeyboardType.Email)
                Spacer(Modifier.height(14.dp))
                LabeledField("Password", initial = "longpassword12", keyboard = KeyboardType.Password, password = true)

                Spacer(Modifier.height(18.dp))
                CopperButton("Sign in", onClick = onSignIn)
                GhostButton("Forgot password", onClick = { /* demo */ })
            }

            Spacer(Modifier.height(24.dp))
            Eyebrow("v 4.2 · Crew portal", color = LLColors.Muted)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    initial: String,
    keyboard: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
) {
    var value by remember { mutableStateOf(initial) }
    Column {
        Eyebrow(label, color = LLColors.Muted)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(LLColors.Parchment2, RoundedCornerShape(12.dp))
                .border(1.dp, LLColors.Line, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { value = it },
                singleLine = true,
                visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
                textStyle = LLType.Body.copy(color = LLColors.Ink),
                keyboardOptions = KeyboardOptions(keyboardType = keyboard),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
