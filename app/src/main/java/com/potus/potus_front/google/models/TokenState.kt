package com.potus.potus_front.google.models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.potus.potus_front.API.response.*
import com.potus.potus_front.API.response.data_models.GardenUser
import java.util.*
import com.potus.potus_front.API.response.GasesResponse
import com.potus.potus_front.API.response.PotusResponse
import com.potus.potus_front.API.response.UserResponse
import com.potus.potus_front.API.response.data_models.GasRegistry
import com.potus.potus_front.API.response.data_models.Meeting
import com.potus.potus_front.API.response.data_models.UserGardenInfo
import com.potus.potus_front.API.response.data_models.UserTrophy

class TokenStateViewModel: ViewModel(){

    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)
    var token: String? by mutableStateOf(null)
    var user: UserResponse? by mutableStateOf(null)
    var location: Pair<Double,Double> by mutableStateOf(value = Pair(0.0,0.0))
    var gases: GasesResponse by mutableStateOf(value = GasesResponse("", 0.0, 0.0, "", registry = mapOf(Pair("", GasRegistry("", "NO DATA", "mg_m3", 0.0)))))
    var gardens: List<NewGardenResponse> by mutableStateOf(value = listOf(NewGardenResponse(0, "No Gardens available", 0, "There are no Gardens available.")))
    var invitations: List<NewGardenResponse> by mutableStateOf(value = listOf(NewGardenResponse(0, "No invitations available", 0, "You have not received any invitations to join a Garden.")))
    var petitions: List<GardenMemberResponse> by mutableStateOf(value = listOf(GardenMemberResponse(garden = NewGardenResponse(0, "GARDEN", 0, ""), "OWNER", GardenUser(0, "", 0, PotusResponse(actions = mapOf(Pair("watering", ActionResponse(lastTime = Date(21/11/2022)))), alive = true, createdDate = Date(21/11/2022), currencyGenerators = mapOf(Pair("", 0)), 0, 0, 0, 0, ignored = false, infested = false, lastModified = Date(21/11/2022), "potus", 0, 0, "", 0, 0), "", "The Garden does not have any requests", ""))))
    var meetings: List<Meeting> by mutableStateOf(value = emptyList())


    fun getState(): String {
        if(user == null)
            return "NOTLOGGED"
        return user!!.user.status
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
            this.user?.user?.potus = potus
        }
    }

    fun myGarden(garden: NewGardenResponse?){
        if (garden != null) {
            this.user?.user?.garden_info = UserGardenInfo(createdDate = Date(), garden = garden, role = "OWNER")
        }
    }

    fun regionalGases(gases: GasesResponse?){
        if (gases != null) {
            this.gases = gases
        }
    }

    fun myLocation(location: Pair<Double,Double>){
        this.location = location
    }

    fun allGardens(gardens: List<NewGardenResponse>?) {
        if (gardens != null) {
            this.gardens = gardens
        }
    }

    fun allMeetings(meetings: List<Meeting>?) {
        if (meetings != null) {
            this.meetings = meetings
        }
    }

    fun myInvitations(invitations: List<NewGardenResponse>?){
        if (invitations != null) {
            this.invitations = invitations
        }
    }

    fun myPetitions(petitions: List<GardenMemberResponse>?){
        if (petitions != null) {
            this.petitions = petitions
        }
    }

    fun myTrophies(trophies: List<UserTrophy>?){
        if (trophies != null) {
            this.user?.trophies = trophies
        }
    }
}

val TokenState = compositionLocalOf<TokenStateViewModel> { error("Token state context not found")}