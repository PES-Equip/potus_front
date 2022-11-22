package com.potus.potus_front.ui.screens
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.ui.unit.dp
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.models.TokenState
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
            AuthScreen( navController ) //onNavigateToSwitcher = { navController.navigate(SwitcherScreen.route) } )
        }
        composable(SwitcherScreen.route) {
            Switcher (
                onNavigateToAuth = { navController.navigate(AuthScreen.route) },
                onNavigateToRegister = { navController.navigate(RegisterScreen.route) },
                onNavigateToHome = { navController.navigate(HomeScreen.route) }
            )
        }
        composable(RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(HomeScreen.route) {
            HomeScreen()
        }
        composable(route = ProfileScreen.route) {
            ProfileScreen()
        }
    }
}

@Composable
fun Switcher(onNavigateToAuth: () -> Unit, onNavigateToRegister: () -> Unit, onNavigateToHome: () -> Unit) {
    val tokenState = TokenState.current

    if(tokenState.isLoggedIn) {
        LaunchedEffect(tokenState.token) {
            val call = getRetrofit().create(APIService::class.java)
                .getUser("Bearer "+ tokenState.token, "user/profile")

            if (call.isSuccessful) {
                tokenState.signUser(call.body())
            }
        }
    } else {
        onNavigateToAuth
    }

    when(tokenState.getState()) {
        "NEW" -> onNavigateToRegister
        "CONFIRMED" -> onNavigateToHome
    }
}