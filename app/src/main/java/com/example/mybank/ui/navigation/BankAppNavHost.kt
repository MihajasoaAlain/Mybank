package com.example.mybank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mybank.ui.screens.admin.AdminScreen
import com.example.mybank.ui.screens.main.MainScreen

// Routes principales de l'application
object BankAppDestinations {
    const val ADMIN_ROUTE = "admin"
    const val MAIN_ROUTE = "main"
}

@Composable
fun BankAppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BankAppDestinations.ADMIN_ROUTE
    ) {
        composable(BankAppDestinations.ADMIN_ROUTE) {
            AdminScreen(
                onNavigateToMain = {
                    navController.navigate(BankAppDestinations.MAIN_ROUTE)
                }
            )
        }
        composable(BankAppDestinations.MAIN_ROUTE) {
            MainScreen()
        }
    }
}
