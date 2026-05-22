package com.example.expensetrackerai.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetrackerai.features.analytics.AnalyticsScreen
import com.example.expensetrackerai.features.auth.AuthViewModel
import com.example.expensetrackerai.features.auth.OnboardingScreen
import com.example.expensetrackerai.features.auth.SplashScreen
import com.example.expensetrackerai.features.dashboard.DashboardScreen
import com.example.expensetrackerai.features.transactions.AddTransactionScreen

import com.example.expensetrackerai.features.settings.ProfileScreen
import com.example.expensetrackerai.features.settings.SettingsViewModel

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector = Icons.Default.Circle) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Analytics : Screen("analytics", "Insights", Icons.Default.Analytics)
    object Budgets : Screen("budgets", "Budgets", Icons.Default.AccountBalanceWallet)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object AddTransaction : Screen("add_transaction", "Add", Icons.Default.Add)
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    
    val mainScreens = listOf(
        Screen.Dashboard,
        Screen.Analytics,
        Screen.Budgets,
        Screen.Profile
    )

    // Handle Auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Onboarding -> {
                navController.navigate(Screen.Onboarding.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
            is AuthViewModel.AuthState.Authenticated -> {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            if (authState == AuthViewModel.AuthState.Authenticated && mainScreens.any { it.route == currentDestination?.route }) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    mainScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onFinished = {
                    authViewModel.onSplashFinished()
                })
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(onFinished = {
                    authViewModel.onOnboardingFinished()
                })
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onAddTransactionClick = {
                        navController.navigate(Screen.AddTransaction.route)
                    },
                    onAnalyticsClick = {
                        navController.navigate(Screen.Analytics.route)
                    }
                )
            }
            composable(Screen.AddTransaction.route) {
                AddTransactionScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.Budgets.route) {
                PlaceholderScreen("Budgeting")
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "$name coming soon!", style = MaterialTheme.typography.titleLarge)
    }
}
