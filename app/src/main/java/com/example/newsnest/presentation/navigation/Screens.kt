package com.example.newsnest.presentation.navigation

sealed class Screens(val route: String) {
    object Login: Screens("login")
    object Registration: Screens("registration")
}