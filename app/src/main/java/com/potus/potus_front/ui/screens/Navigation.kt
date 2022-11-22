package com.potus.potus_front.ui.screens
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.potus.potus_front.ui.screens.Screen.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun Navigation(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AuthScreen.route){
        composable(AuthScreen.route){
            AuthScreen(navController)
        }
        composable(RegisterScreen.route){
            RegisterScreen(navController)
        }
        composable(HomeScreen.route){
            HomeScreen()
        }
        composable(route= ProfileScreen.route){
            ProfileScreen()
        }
        composable(SwitcherScreen.route){
            ApplicationSwitcher(navController)
        }
    }
}
