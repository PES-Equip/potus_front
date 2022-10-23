package com.potus.potus_front.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi

import androidx.navigation.compose.rememberNavController
import com.potus.potus_front.google.AuthViewModel
import com.potus.potus_front.screens.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Navigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(
    navController = navController,
    startDestination = Routes.Login.route
    ) {
        composable(Routes.Home.route) {
            HomeScreen()
        }
        composable(Routes.Login.route){
            AuthScreen()
        }
    }
}
