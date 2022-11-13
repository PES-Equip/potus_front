package com.potus.potus_front.ui.screens

sealed class Screen(val route: String){
    object MainScreen : Screen("main_screen")
    object DetailScreen : Screen("detail_screen")
    object AuthScreen : Screen("auth_screen")
    object ProfileScreen : Screen("profile_screen")
    object HomeScreen : Screen("home_screen")
    object RegisterScreen : Screen("register_screen")
    object Pantalla1 : Screen("pantalla1")


    fun withArgs(vararg args: String) : String{
        return buildString{
            append(route)
            args.forEach{ arg ->
                append("/$arg")

            }
        }
    }
}
