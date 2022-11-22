package com.potus.potus_front.models

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.potus.potus_front.API.response.GasesResponse
import com.potus.potus_front.API.response.PotusResponse
import com.potus.potus_front.API.response.UserResponse
import com.potus.potus_front.API.response.data_models.Gas
import com.potus.potus_front.API.response.data_models.Registry

class TokenStateViewModel: ViewModel(){

    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)
    var token: String? by mutableStateOf(null)
    var user: UserResponse? by mutableStateOf(null)
    var gases: GasesResponse by mutableStateOf(value = GasesResponse("", 0.0, 0.0, "", registry = Registry(registry = listOf(Gas("", "", "mg_m3", 0.0)))))
    var location: Pair<Double,Double> by mutableStateOf(value=Pair(0.0,0.0))

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

    fun regionalGases(gases: GasesResponse?){
        if (gases != null) {
            this.gases = gases!!
        }
    }

    fun myLocation(location: Pair<Double,Double>){
        this.location = location
    }
}

val TokenState = compositionLocalOf<TokenStateViewModel> { error("Token state context not found")}