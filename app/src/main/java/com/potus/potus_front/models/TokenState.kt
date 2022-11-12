package com.potus.potus_front.models

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.potus.potus_front.API.response.PotusResponse
import com.potus.potus_front.API.response.UserResponse

class TokenStateViewModel: ViewModel(){

    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)
    var token: String? by mutableStateOf(null)
    var user: UserResponse? by mutableStateOf(null)


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
}

val TokenState = compositionLocalOf<TokenStateViewModel> { error("Token state context not found")}