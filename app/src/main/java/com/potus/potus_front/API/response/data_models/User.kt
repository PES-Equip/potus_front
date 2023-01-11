package com.potus.potus_front.API.response.data_models

import androidx.annotation.Nullable
import com.potus.potus_front.API.response.PotusResponse

data class User(
    var admin: Boolean,
    var currency: Int,
    var email: String,
    var garden_info: UserGardenInfo?,
    var id: Int,
    var meetings: List<Meeting>,
    var potus: PotusResponse,
    var status: String,
    var username: String
)