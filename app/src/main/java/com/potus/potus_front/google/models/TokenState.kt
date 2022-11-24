package com.potus.potus_front.google.models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.potus.potus_front.API.response.*
import com.potus.potus_front.API.response.data_models.GardenUser
import java.util.*

class TokenStateViewModel: ViewModel(){

    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)
    var token: String? by mutableStateOf(null)
    var user: UserResponse? by mutableStateOf(null)
    var location: Pair<Double,Double> by mutableStateOf(value = Pair(0.0,0.0))
    var gardens: List<NewGardenResponse> by mutableStateOf(value = listOf(NewGardenResponse("No Gardens available", 0, "There are no Gardens available.")))
    var invitations: List<NewGardenResponse> by mutableStateOf(value = listOf(NewGardenResponse("No invitations available", 0, "You have not received any invitations to join a Garden.")))
    var petitions: List<GardenMemberResponse> by mutableStateOf(value = listOf(GardenMemberResponse(garden = NewGardenResponse("GARDEN", 0, ""), "OWNER", GardenUser(0, "", 0, PotusResponse(actions = mapOf(Pair("watering", ActionResponse(lastTime = Date(21/11/2022)))), alive = true, createdDate = Date(21/11/2022), currencyGenerators = listOf(Pair("", 0)), 0, 0, 0, 0, ignored = false, infested = false, lastModified = Date(21/11/2022), "potus", 0, 0, "", 0, 0), "", "The Garden does not have any requests", ""))))

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

    fun myPetitions(petitions: GardenPetitionsResponse?){
        this.petitions = petitions?.petitions!!
    }
}

val TokenState = compositionLocalOf<TokenStateViewModel> { error("Token state context not found")}