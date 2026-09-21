package com.lordsandlegends.crew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lordsandlegends.crew.data.Contract
import com.lordsandlegends.crew.data.model.Staff
import com.lordsandlegends.crew.data.model.StaffRole
import com.lordsandlegends.crew.data.model.TutorialState
import com.lordsandlegends.crew.ui.components.BottomTabBar
import com.lordsandlegends.crew.ui.components.Routes
import com.lordsandlegends.crew.ui.components.VideoSheet
import com.lordsandlegends.crew.ui.components.VideoSheetState
import com.lordsandlegends.crew.ui.screens.AcademyScreen
import com.lordsandlegends.crew.ui.screens.ClockStatus
import com.lordsandlegends.crew.ui.screens.Hrscreen
import com.lordsandlegends.crew.ui.screens.LeaveManagementScreen
import com.lordsandlegends.crew.ui.screens.LoginScreen
import com.lordsandlegends.crew.ui.screens.OnboardingDetailsScreen
import com.lordsandlegends.crew.ui.screens.OnboardingOffboardingScreen
import com.lordsandlegends.crew.ui.screens.OverviewScreen
import com.lordsandlegends.crew.ui.screens.PerformanceScreen
import com.lordsandlegends.crew.ui.screens.PoliciesScreen
import com.lordsandlegends.crew.ui.screens.ProfileScreen
import com.lordsandlegends.crew.ui.screens.SelfServiceScreen
import com.lordsandlegends.crew.ui.screens.SignContractScreen
import com.lordsandlegends.crew.ui.screens.TimeAttendanceScreen
import com.lordsandlegends.crew.ui.screens.UserPased
import com.lordsandlegends.crew.ui.screens.WarningsPerformanceScreen
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LordsAndLegendsTheme

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
    var signedInStaff by remember { mutableStateOf<Staff?>(null) }

    // Login sits outside the graph, so there is no back route into it once you are in.
    when (val staff = signedInStaff) {
        null -> LoginScreen(onSignedIn = { signedInStaff = it })
        else -> CrewNavGraph(staff = staff, onSignOut = { signedInStaff = null })
    }
}

@Composable
private fun CrewNavGraph(staff: Staff, onSignOut: () -> Unit) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // shared contract list, still in memory until the contract repository lands
    val contracts = remember { mutableStateListOf<Contract>() }
    var sheet by remember { mutableStateOf<VideoSheetState?>(null) }

    // clock status (UI only — a manager's accept or decline will drive this via Supabase)
    var clockStatus by rememberSaveable { mutableStateOf(ClockStatus.OFF_SHIFT) }

    val isManager = staff.role != StaffRole.EMPLOYEE

    // A new starter goes through policies and onboarding first; everyone else lands
    // on the overview.
    val startRoute = if (staff.tutorialState == TutorialState.NOT_STARTED) {
        Routes.POLICIES
    } else {
        Routes.OVERVIEW
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LLColors.Parchment2)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = startRoute,
                modifier = Modifier.weight(1f),
            ) {
                composable(Routes.POLICIES) {
                    PoliciesScreen(onNext = { navController.navigate(Routes.ONBOARDING_DETAILS) })
                }

                composable(Routes.ONBOARDING_DETAILS) {
                    OnboardingDetailsScreen(
                        onSubmit = {
                            // onboarding is done, so it should not sit behind the back button
                            navController.navigate(Routes.OVERVIEW) {
                                popUpTo(startRoute) { inclusive = true }
                            }
                        },
                        onBack = { navController.popBackStack() },
                    )
                }

                composable(Routes.OVERVIEW) {
                    OverviewScreen(
                        role = staff.role,
                        onAcademy = { navController.navigate(Routes.ACADEMY) },
                        onPerformance = { navController.navigate(Routes.PERFORMANCE) },
                        onContracts = { navController.navigate(Routes.CONTRACTS) },
                        onWarnings = { navController.navigate(Routes.WARNINGS) },
                        onLeaveManagement = { navController.navigate(Routes.LEAVE) },
                        onOnboardingOffboarding = { navController.navigate(Routes.ONBOARDING_OFFBOARDING) },
                        onTimeAttendance = { navController.navigate(Routes.TIME_ATTENDANCE) },
                        onSelfService = { navController.navigate(Routes.SELF_SERVICE) },
                    )
                }

                composable(Routes.ACADEMY) {
                    AcademyScreen(
                        onBack = { navController.popBackStack() },
                        onPlayVideo = { sheet = it },
                    )
                }

                composable(Routes.PERFORMANCE) {
                    PerformanceScreen(onBack = { navController.popBackStack() })
                }

                composable(Routes.PROFILE) {
                    ProfileScreen(onSignOut = onSignOut)
                }

                composable(Routes.PASSED) { UserPased() }

                composable(Routes.TIME_ATTENDANCE) {
                    TimeAttendanceScreen(
                        onBack = { navController.popBackStack() },
                        status = clockStatus,
                        onStartShift = { clockStatus = ClockStatus.CLOCK_IN_PENDING },
                        onCancelRequest = {
                            clockStatus = if (clockStatus == ClockStatus.BREAK_PENDING) {
                                ClockStatus.ON_SHIFT
                            } else {
                                ClockStatus.OFF_SHIFT
                            }
                        },
                        onRequestBreak = { clockStatus = ClockStatus.BREAK_PENDING },
                        onEndBreak = { clockStatus = ClockStatus.ON_SHIFT },
                        onEndShift = { clockStatus = ClockStatus.OFF_SHIFT },
                    )
                }

                composable(Routes.SELF_SERVICE) {
                    SelfServiceScreen(
                        onBack = { navController.popBackStack() },
                        showManagerTab = isManager,
                    )
                }

                // Manager and owner destinations. An employee's graph does not contain
                // these routes at all, so there is no way to reach them — the navigation
                // half of the access control, with Kendal's RLS as the other half.
                if (isManager) {
                    composable(Routes.CONTRACTS) {
                        Hrscreen(
                            contracts = contracts,
                            onContractAdded = { contracts.add(it) },
                            onContractTapped = { navController.navigate(Routes.signContract(it.id)) },
                        )
                    }

                    composable(
                        route = Routes.SIGN_CONTRACT,
                        arguments = listOf(navArgument("contractId") { type = NavType.StringType }),
                    ) { entry ->
                        val contractId = entry.arguments?.getString("contractId")
                        val contract = contracts.firstOrNull { it.id == contractId }
                        if (contract != null) {
                            SignContractScreen(
                                contract = contract,
                                onSigned = { navController.popBackStack() },
                            )
                        } else {
                            // contract is gone (after process death, say) — go back, don't crash
                            LaunchedEffect(contractId) { navController.popBackStack() }
                        }
                    }

                    composable(Routes.WARNINGS) {
                        WarningsPerformanceScreen(onBack = { navController.popBackStack() })
                    }

                    composable(Routes.LEAVE) {
                        LeaveManagementScreen(onBack = { navController.popBackStack() })
                    }

                    composable(Routes.ONBOARDING_OFFBOARDING) {
                        OnboardingOffboardingScreen(onBack = { navController.popBackStack() })
                    }
                }
            }

            if (currentRoute != null && currentRoute !in Routes.fullScreen) {
                BottomTabBar(
                    currentRoute = currentRoute,
                    onSelect = { route -> navController.switchTab(route) },
                )
            }
        }

        sheet?.let { VideoSheet(state = it, onDismiss = { sheet = null }) }
    }
}

/**
 * Tab switching should not stack screens behind the back button, and returning to a
 * tab should find it where you left it.
 */
private fun NavHostController.switchTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
