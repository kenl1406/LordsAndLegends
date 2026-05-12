package com.lordsandlegends.crew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lordsandlegends.crew.ui.components.BottomTabBar
import com.lordsandlegends.crew.ui.components.Screen
import com.lordsandlegends.crew.ui.components.VideoSheet
import com.lordsandlegends.crew.ui.components.VideoSheetState
import com.lordsandlegends.crew.ui.screens.AcademyScreen
import com.lordsandlegends.crew.ui.screens.LoginScreen
import com.lordsandlegends.crew.ui.screens.OverviewScreen
import com.lordsandlegends.crew.ui.screens.PerformanceScreen
import com.lordsandlegends.crew.ui.screens.ProfileScreen
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LordsAndLegendsTheme
import com.lordsandlegends.crew.ui.screens.UserPased

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            LordsAndLegendsTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot() {
    var current by rememberSaveable { mutableStateOf(Screen.PASSED) }
    var sheet by rememberSaveable { mutableStateOf<VideoSheetState?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LLColors.Parchment2)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = current,
                transitionSpec = {
                    (fadeIn(tween(220)) + slideInVertically(tween(220)) { it / 24 })
                        .togetherWith(fadeOut(tween(140)))
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                label = "screen"
            ) { screen ->
                when (screen) {
                    Screen.PASSED -> UserPased()
                    // This tells the app: if the state is HR, show this screen
                    Screen.Login -> LoginScreen(onSignIn = { current = Screen.Overview })
                    Screen.Overview -> OverviewScreen(
                        onAcademy = { current = Screen.Academy },
                        onPerformance = { current = Screen.Performance }
                    )
                    Screen.Academy -> AcademyScreen(
                        onBack = { current = Screen.Overview },
                        onPlayVideo = { sheet = it }
                    )
                    Screen.Performance -> PerformanceScreen(
                        onBack = { current = Screen.Overview }
                    )
                    Screen.Profile -> ProfileScreen(
                        onSignOut = { current = Screen.Login }
                    )
                }
            }
// this will make it so the nav bar will always be displayed unless
            if (current != Screen.Login ) {
                BottomTabBar(
                    current = current,
                    onSelect = { current = it }
                )
            }
        }

        sheet?.let {
            VideoSheet(state = it, onDismiss = { sheet = null })
        }
    }
}
