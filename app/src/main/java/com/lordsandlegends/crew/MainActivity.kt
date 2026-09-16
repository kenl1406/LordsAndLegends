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
import com.lordsandlegends.crew.ui.screens.PoliciesScreen
import com.lordsandlegends.crew.ui.screens.OnboardingDetailsScreen
import com.lordsandlegends.crew.data.Contract
import com.lordsandlegends.crew.ui.screens.Hrscreen
import com.lordsandlegends.crew.ui.screens.SignContractScreen
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.lordsandlegends.crew.ui.screens.WarningsPerformanceScreen
import com.lordsandlegends.crew.ui.screens.LeaveManagementScreen
import com.lordsandlegends.crew.ui.screens.OnboardingOffboardingScreen
import com.lordsandlegends.crew.ui.screens.TimeAttendanceScreen
import com.lordsandlegends.crew.ui.screens.SelfServiceScreen
import com.lordsandlegends.crew.ui.screens.ClockStatus

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
    var current by rememberSaveable { mutableStateOf(Screen.Login) }
    var sheet by rememberSaveable { mutableStateOf<VideoSheetState?>(null) }

    // shared contract list + which one is being signed
    val contracts = remember { mutableStateListOf<Contract>() }
    var selectedContract by remember { mutableStateOf<Contract?>(null) }

    // clock-in status (UI only — manager accept/decline will update this via Supabase later)
    var clockStatus by rememberSaveable { mutableStateOf(ClockStatus.OFF_SHIFT) }

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
                modifier = Modifier.weight(1f).fillMaxSize(),
                label = "screen"
            ) { screen ->
                when (screen) {
                    Screen.PASSED -> UserPased()
                    Screen.Login -> LoginScreen(onSignIn = { current = Screen.Policies })
                    Screen.Policies -> PoliciesScreen(onNext = { current = Screen.OnboardingDetails })
                    Screen.OnboardingDetails -> OnboardingDetailsScreen(
                        onSubmit = { current = Screen.Overview },
                        onBack = { current = Screen.Policies },
                    )
                    Screen.Overview -> OverviewScreen(
                        onAcademy = { current = Screen.Academy },
                        onPerformance = { current = Screen.Performance },
                        onContracts = { current = Screen.Contracts },
                        onWarnings = { current = Screen.Warnings },
                        onLeaveManagement = { current = Screen.LeaveManagement },
                        onOnboardingOffboarding = { current = Screen.OnboardingOffboarding },
                        onTimeAttendance = { current = Screen.TimeAttendance },
                        onSelfService = { current = Screen.SelfService },
                    )
                    Screen.Academy -> AcademyScreen(
                        onBack = { current = Screen.Overview },
                        onPlayVideo = { sheet = it }
                    )
                    Screen.Performance -> PerformanceScreen(onBack = { current = Screen.Overview })
                    Screen.Profile -> ProfileScreen(onSignOut = { current = Screen.Login })

                    Screen.Contracts -> Hrscreen(
                        contracts = contracts,
                        onContractAdded = { contracts.add(it) },
                        onContractTapped = {
                            selectedContract = it
                            current = Screen.SignContract
                        },
                    )
                    Screen.SignContract -> selectedContract?.let { contract ->
                        SignContractScreen(
                            contract = contract,
                            onSigned = { current = Screen.Contracts },
                        )
                    }

                    Screen.Warnings -> WarningsPerformanceScreen(
                        onBack = { current = Screen.Overview }


                    )

                    Screen.LeaveManagement -> LeaveManagementScreen(
                        onBack = { current = Screen.Overview }
                    )

                    Screen.OnboardingOffboarding -> OnboardingOffboardingScreen(
                        onBack = { current = Screen.Overview }
                    )

                    Screen.TimeAttendance -> TimeAttendanceScreen(
                        onBack = { current = Screen.Overview },
                        status = clockStatus,
                        onStartShift = { clockStatus = ClockStatus.CLOCK_IN_PENDING },
                        onCancelRequest = {
                            clockStatus = if (clockStatus == ClockStatus.BREAK_PENDING) ClockStatus.ON_SHIFT else ClockStatus.OFF_SHIFT
                        },
                        onRequestBreak = { clockStatus = ClockStatus.BREAK_PENDING },
                        onEndBreak = { clockStatus = ClockStatus.ON_SHIFT },
                        onEndShift = { clockStatus = ClockStatus.OFF_SHIFT },
                    )

                    Screen.SelfService -> SelfServiceScreen(
                        onBack = { current = Screen.Overview }
                    )
                }
            }

            if (current != Screen.Login && current != Screen.Policies &&
                current != Screen.OnboardingDetails && current != Screen.SignContract
            ) {
                BottomTabBar(current = current, onSelect = { current = it })
            }
        }

        sheet?.let { VideoSheet(state = it, onDismiss = { sheet = null }) }
    }
}