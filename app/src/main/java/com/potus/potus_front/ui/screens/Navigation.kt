package com.potus.potus_front.ui.screens
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.screens.Screen.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun Navigation(navController : NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = AuthScreen.route) {
        composable(AuthScreen.route){
            AuthScreen( onNavigateToSwitcher = { navController.navigate(SwitcherScreen.route) } )
        }
        composable(SwitcherScreen.route) {
            val tokenState = TokenState.current

            if(tokenState.isLoggedIn) {
                LaunchedEffect(tokenState.token) {
                    val call = getRetrofit().create(APIService::class.java)
                        .getUser("Bearer "+ tokenState.token, "user/profile")

                    if (call.isSuccessful) {
                        tokenState.signUser(call.body())
                        when(tokenState.getState()) {
                            "NEW" -> navController.navigate(RegisterScreen.route)
                            "CONFIRMED" -> {
                                when (tokenState.user?.user?.potus?.alive) {
                                    true -> {
                                        navController.navigate(HomeScreen.route)
                                    }
                                    false -> {
                                        navController.navigate(RevivePopup.route)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                navController.navigate(AuthScreen.route)
            }
        }
        composable(RegisterScreen.route) {
            RegisterScreen(onNavigateToHome = { navController.navigate(HomeScreen.route) })
        }
        composable(HomeScreen.route) {
            HomeScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                onNavigateToSelection = { navController.navigate(SelectGardenScreen.route) },
                onNavigateToShop = { navController.navigate(ShopScreen.route) }
            )
            }
        composable(RevivePopup.route) {
            RevivePopup(onNavigateToHome = { navController.navigate(HomeScreen.route) })
        }
        composable(ShopScreen.route) {
            ShopScreen(onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) })
        }
        composable(MeetingsScreen.route) {
            MeetingsScreen(onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToInvitations = { navController.navigate(InvitationsToGardensScreen.route) },
                onNavigateToCreation = { navController.navigate(CreateGardenScreen.route) },
                onNavigateToShop = { navController.navigate(ShopScreen.route) }
            )
        }
        composable(route = ProfileScreen.route) {
            ProfileScreen(
                    onNavigateToHome = { navController.navigate(HomeScreen.route) },
                    onNavigateToAuth = { navController.navigate(AuthScreen.route) },
                    onNavigateToHistory = { navController.navigate(HistoryScreen.route) }
            )
        }
        composable(HistoryScreen.route) {
            HistoryScreen(onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToShop = { navController.navigate(ShopScreen.route) })
        }
        composable(route = SelectGardenScreen.route) {
            SelectGardenScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToInvitations = { navController.navigate(InvitationsToGardensScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToCreation = { navController.navigate(CreateGardenScreen.route) },
                onNavigateToShop = { navController.navigate(ShopScreen.route) }
            )
        }
        composable(route = CreateGardenScreen.route) {
            CreateGardenScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                onNavigateToInvitations = { navController.navigate(InvitationsToGardensScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToSelection = { navController.navigate(SelectGardenScreen.route) },
                onNavigateToShop = { navController.navigate(ShopScreen.route) }
            )
        }
        composable(route = InvitationsToGardensScreen.route) {
            InvitationsToGardensScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                onNavigateToInvitations = { navController.navigate(InvitationsToGardensScreen.route) },
                onNavigateToSelection = { navController.navigate(SelectGardenScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToCreation = { navController.navigate(CreateGardenScreen.route) },
                onNavigateToShop = { navController.navigate(ShopScreen.route) }
            )
        }
        composable(route = GardenScreen.route) {
            GardenScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                //TO BE IMPLEMENTED (CURRENTLY LEADING BACK TO GARDEN SCREEN)
                onNavigateToShop = { navController.navigate(ShopScreen.route) },
                onNavigateToManagement = { navController.navigate(GardenManagementScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                //TO BE IMPLEMENTED (CURRENTLY LEADING BACK TO GARDEN SCREEN)
                onNavigateToMeetings = { navController.navigate(MeetingsScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToChat = { navController.navigate(ChatScreen.route) }
            )
        }
        composable(route = GardenManagementScreen.route) {
            GardenManagementScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToPetitions = { navController.navigate(PetitionsToGardensScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                onNavigateToShop = { navController.navigate(ShopScreen.route) }
            )
        }
        composable(route = PetitionsToGardensScreen.route) {
            PetitionsToGardensScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToPetitions = { navController.navigate(PetitionsToGardensScreen.route) },
                onNavigateToManagement = { navController.navigate(GardenManagementScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                onNavigateToShop = { navController.navigate(ShopScreen.route) }
            )
        }
        composable(route = ChatScreen.route){
            ChatScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToShop = { navController.navigate(GardenScreen.route) },
                onNavigateToChat = { navController.navigate(ChatScreen.route) },
                //TO BE IMPLEMENTED (CURRENTLY LEADING BACK TO GARDEN SCREEN)
                onNavigateToMeetings = { navController.navigate(GardenScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) }
            )

        }
    }
}