package com.potus.potus_front.navigation

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Login : Routes("login")
    object Register : Routes("register")
}