package com.potus.potus_front.API.response

import com.potus.potus_front.API.response.data_models.UserGardenInfo

data class UserResponse(
    var currency: Int,
    var email: String,
    var garden_info: UserGardenInfo,
    var id: Int,
    var potus: PotusResponse,
    var status: String,
    var username: String,
    var message: String
)