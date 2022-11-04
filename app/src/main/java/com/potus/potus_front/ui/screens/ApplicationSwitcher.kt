package com.potus.potus_front.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.models.TokenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun ApplicationSwitcher() {
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
        AuthScreen()
    }

    when(tokenState.getState()) {
        "NEW" -> RegisterScreen()
        "CONFIRMED" -> ProfileScreen()
    }

}