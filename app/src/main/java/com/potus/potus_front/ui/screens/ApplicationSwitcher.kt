package com.potus.potus_front.ui.screens

import android.media.session.MediaSession.Token
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.models.TokenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timer

var TokenFet=true;
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable

fun ApplicationSwitcher(navController: NavController) {
    val tokenState = TokenState.current

    if(tokenState.isLoggedIn){

        LaunchedEffect(tokenState.token) {
            val call = getRetrofit().create(APIService::class.java)
                .getUser("Bearer "+ tokenState.token, "user/profile")

            if (call.isSuccessful) {
                tokenState.signUser(call.body())
            }
        }
    } else {
        navController.navigate("auth_screen")
    }

    if(TokenFet){
        if(tokenState.getState()== "NEW") {
            TokenFet=false;
            navController.navigate("register_screen")
        }
        else if(tokenState.getState()== "CONFIRMED"){
            TokenFet=false;
            navController.navigate("profile_screen")
        }
    }

}