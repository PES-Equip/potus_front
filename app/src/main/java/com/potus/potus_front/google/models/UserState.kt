package com.potus.potus_front.google.models

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.potus.potus_front.API.response.UserResponse

class UserStateViewModel: ViewModel(){

    var user: UserResponse? by mutableStateOf(null)


    fun getState(): String {
        if(user == null)
            return "NOTLOGGED"

        return user!!.status
    }


    fun signIn(user: UserResponse?){

        this.user = user
    }

}

val UserState = compositionLocalOf<TokenStateViewModel> { error("Token state context not found")}