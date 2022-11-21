package com.potus.potus_front.models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.potus.potus_front.API.response.*

class TokenStateViewModel: ViewModel(){

    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)
    var token: String? by mutableStateOf(null)
    var user: UserResponse? by mutableStateOf(null)
    var location: Pair<Double,Double> by mutableStateOf(value = Pair(0.0,0.0))
    var gardens: List<NewGardenResponse> by mutableStateOf(value = listOf(NewGardenResponse("NoGardensAvailable", 0, "There are no Gardens available.")))
    var invitations: List<NewGardenResponse> by mutableStateOf(value = listOf(NewGardenResponse("No invitations available", 0, "You have not received any invitations to join a Garden.")))

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

    fun allGardens(gardens: GardenListResponse?) {
        this.gardens = gardens?.gardens!!
    }

    fun myInvitations(invitations: GardenListResponse?){
        this.invitations = invitations?.gardens!!
    }
}

val TokenState = compositionLocalOf<TokenStateViewModel> { error("Token state context not found")}