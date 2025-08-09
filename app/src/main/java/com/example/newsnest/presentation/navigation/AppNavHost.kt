package com.example.newsnest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsnest.presentation.login.LoginScreen
import com.example.newsnest.presentation.registration.RegistrationScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screens.Login.route) {
        composable(route = Screens.Login.route) {
            LoginScreen(onNavigate = { navController.navigate(Screens.Registration.route) })
        }
        composable(route = Screens.Registration.route) {
            RegistrationScreen(onNavigate = { navController.popBackStack() })
        }
    }
}