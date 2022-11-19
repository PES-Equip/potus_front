package com.potus.potus_front.models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.potus.potus_front.API.response.GardenListResponse
import com.potus.potus_front.API.response.PotusResponse
import com.potus.potus_front.API.response.UserResponse

class TokenStateViewModel: ViewModel(){

    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)
    var token: String? by mutableStateOf(null)
    var user: UserResponse? by mutableStateOf(null)
    var location: Pair<Double,Double> by mutableStateOf(value = Pair(0.0,0.0))
    var gardens: List<Triple<String, Int, String>> by mutableStateOf(value = listOf(Triple("No Gardens available", 0, "There are no Gardens available.")))

    fun getState(): String {
        if(user == null)
            return "NOTLOGGED"
        return user!!.status
    }

    fun signUser(user: UserResponse?){
        this.user = user
    }

    fun signToken(newToken: String){
        this.token = newToken
        isBusy = true
        isLoggedIn = true
        isBusy = false
    }

    fun signOut() {
        isBusy = true
        isLoggedIn = false
        isBusy = false
        token = null
        user = null
    }

    fun myPotus(potus: PotusResponse?){
        if (potus != null) {
            this.user?.potus = potus
        }
    }

    fun myLocation(location: Pair<Double,Double>){
        this.location = location
    }

    fun allGardens(gardens: List<Triple<String, Int, String>>){
        this.gardens = gardens
    }
}

val TokenState = compositionLocalOf<TokenStateViewModel> { error("Token state context not found")}