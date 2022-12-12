package com.potus.potus_front.ui.screens

sealed class Screen(val route: String){
    object AuthScreen : Screen("auth_screen")
    object ProfileScreen : Screen("profile_screen")
    object HomeScreen : Screen("home_screen")
    object RegisterScreen : Screen("register_screen")
    object SwitcherScreen : Screen("switcher_screen")
    object ShopScreen : Screen("shop_screen")
}
