package com.potus.potus_front.ui.screens

sealed class Screen(val route: String){
    object AuthScreen : Screen("auth_screen")
    object ProfileScreen : Screen("profile_screen")
    object HomeScreen : Screen("home_screen")
    object RegisterScreen : Screen("register_screen")
    object SwitcherScreen : Screen("switcher_screen")
    object ShopScreen : Screen("shop_screen")
    object MeetingsScreen : Screen("meetings_screen")
    object RevivePopup : Screen("revive_popup")
    object HistoryScreen : Screen("history_screen")
    object SelectGardenScreen : Screen("select_garden_screen")
    object CreateGardenScreen : Screen("create_garden_screen")
    object InvitationsToGardensScreen : Screen("invitations_to_gardens_screen")
    object GardenScreen : Screen("garden_screen")
    object GardenManagementScreen : Screen("garden_management_screen")
    object PetitionsToGardensScreen : Screen("petitions_to_gardens_screen")
    object ChatScreen : Screen("chat_screen")
}