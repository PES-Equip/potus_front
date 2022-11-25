package com.potus.potus_front.controllers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.ActionRequest
import com.potus.potus_front.google.models.TokenState
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun getWater(): Int {
    // Here a call to the backend service will be performed to get the actual water level.

    return TokenState.current.user?.potus?.waterLevel ?: 100
}

fun getLeaves(): Int {
    // Here a call to the backend service will be performed to get the actual leaves count.
    return 0
}

fun updateWater(updatedWater: Int): Boolean {
    // Here a call to the backend service will be performed to set the water level.
    if(updatedWater in 0..100) {

        return true
    }
    return false
}

@Composable
fun UpdatePotusAction(action: String) {
    // Here a call to the backend service will be performed to set the leaves count.

}