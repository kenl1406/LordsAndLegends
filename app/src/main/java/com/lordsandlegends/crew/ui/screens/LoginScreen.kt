package com.lordsandlegends.crew.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lordsandlegends.crew.R
import com.lordsandlegends.crew.data.model.Staff
import com.lordsandlegends.crew.ui.components.CopperButton
import com.lordsandlegends.crew.ui.components.Eyebrow
import com.lordsandlegends.crew.ui.components.GhostButton
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType
import com.lordsandlegends.crew.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onSignedIn: (Staff) -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory),
) {
    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Sign-in succeeded and we know who this is — hand the staff member upwards.
    LaunchedEffect(state.staff) {
        state.staff?.let(onSignedIn)
    }

    val canSubmit = email.isNotBlank() && password.isNotBlank() && !state.loading

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
                Text(
                    "Welcome back",
                    style = LLType.Display.copy(fontSize = TextUnit.Unspecified),
                    color = LLColors.Ink,
                )
                Spacer(Modifier.height(4.dp))
                Text("Sign in to your crew account", style = LLType.Body, color = LLColors.Muted)

                Spacer(Modifier.height(20.dp))

                LabeledField(
                    label = "Employee email",
                    value = email,
                    onValueChange = {
                        email = it
                        if (state.error != null) viewModel.clearError()
                    },
                    keyboard = KeyboardType.Email,
                    enabled = !state.loading,
                )
                Spacer(Modifier.height(14.dp))
                LabeledField(
                    label = "Password",
                    value = password,
                    onValueChange = {
                        password = it
                        if (state.error != null) viewModel.clearError()
                    },
                    keyboard = KeyboardType.Password,
                    password = true,
                    enabled = !state.loading,
                )

                state.error?.let { message ->
                    Spacer(Modifier.height(14.dp))
                    ErrorBanner(message)
                }

                Spacer(Modifier.height(18.dp))

                if (state.loading) {
                    LoadingButton()
                } else {
                    CopperButton(
                        text = "Sign in",
                        onClick = { viewModel.signIn(email.trim(), password) },
                        enabled = canSubmit,
                    )
                }
                GhostButton("Forgot password", onClick = { /* not built yet */ })
            }

            Spacer(Modifier.height(24.dp))
            Eyebrow("v 4.2 · Crew portal", color = LLColors.Muted)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ErrorBanner(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Bad.copy(alpha = 0.10f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = LLColors.Bad,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(message, style = LLType.BodySmall, color = LLColors.Bad)
    }
}

@Composable
private fun LoadingButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LLColors.Copper.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(vertical = 14.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(
            color = androidx.compose.ui.graphics.Color.White,
            strokeWidth = 2.dp,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text("Signing in…", style = LLType.Body, color = androidx.compose.ui.graphics.Color.White)
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboard: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
    enabled: Boolean = true,
) {
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
                onValueChange = onValueChange,
                singleLine = true,
                enabled = enabled,
                visualTransformation =
                    if (password) PasswordVisualTransformation() else VisualTransformation.None,
                textStyle = LLType.Body.copy(color = LLColors.Ink),
                keyboardOptions = KeyboardOptions(keyboardType = keyboard),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
