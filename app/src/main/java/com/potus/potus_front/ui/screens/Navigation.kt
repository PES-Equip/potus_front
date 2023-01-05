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
                            "CONFIRMED" -> navController.navigate(HomeScreen.route)
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
            if (TokenState.current.user?.user?.potus?.alive == true) HomeScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                onNavigateToSelection = { navController.navigate(SelectGardenScreen.route) }
            )
            else RevivePopup(onNavigateToHome = { navController.navigate(HomeScreen.route) })
        }
        composable(route = ProfileScreen.route) {
            ProfileScreen(
                    onNavigateToHome = { navController.navigate(HomeScreen.route) },
                    onNavigateToAuth = { navController.navigate(AuthScreen.route) },
                    onNavigateToHistory = { navController.navigate(HistoryScreen.route) }
            )
        }
        composable(HistoryScreen.route) {
            HistoryScreen(onNavigateToProfile = { navController.navigate(ProfileScreen.route) })
        }
        composable(route = SelectGardenScreen.route) {
            SelectGardenScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToInvitations = { navController.navigate(InvitationsToGardensScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToCreation = { navController.navigate(CreateGardenScreen.route) }
            )
        }
        composable(route = CreateGardenScreen.route) {
            CreateGardenScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                onNavigateToInvitations = { navController.navigate(InvitationsToGardensScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToSelection = { navController.navigate(SelectGardenScreen.route) }
            )
        }
        composable(route = InvitationsToGardensScreen.route) {
            InvitationsToGardensScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) },
                onNavigateToInvitations = { navController.navigate(InvitationsToGardensScreen.route) },
                onNavigateToSelection = { navController.navigate(SelectGardenScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToCreation = { navController.navigate(CreateGardenScreen.route) }
            )
        }
        composable(route = GardenScreen.route) {
            GardenScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToManagement = { navController.navigate(GardenManagementScreen.route) },
                //TO BE IMPLEMENTED
                onNavigateToShop = { navController.navigate(GardenScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToChat = { navController.navigate(ChatScreen.route) }
            )
        }
        composable(route = GardenManagementScreen.route) {
            GardenManagementScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToPetitions = { navController.navigate(PetitionsToGardensScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) }
            )
        }
        composable(route = PetitionsToGardensScreen.route) {
            PetitionsToGardensScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToPetitions = { navController.navigate(PetitionsToGardensScreen.route) },
                onNavigateToManagement = { navController.navigate(GardenManagementScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) }
            )
        }
        composable(route = ChatScreen.route){
            ChatScreen(
                onNavigateToProfile = { navController.navigate(ProfileScreen.route) },
                onNavigateToShop = { navController.navigate(GardenScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) },
                onNavigateToGarden = { navController.navigate(GardenScreen.route) }
            )

        }
    }
}