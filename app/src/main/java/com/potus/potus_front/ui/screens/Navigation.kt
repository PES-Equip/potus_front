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
                        //if (tokenState.getState() == "NEW") navController.navigate(RegisterScreen.route)
                        //else if (tokenState.getState() == "CONFIRMED") navController.navigate(HomeScreen.route)
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
            HomeScreen() { navController.navigate(ProfileScreen.route) }
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
    }
}