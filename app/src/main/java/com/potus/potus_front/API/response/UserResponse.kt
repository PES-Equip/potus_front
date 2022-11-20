package com.potus.potus_front.API.response

data class UserResponse(
    var username: String,
    var email: String,
    var garden_info: NewGardenResponse,
    var currency: Int,
    var potus: PotusResponse,
    var status: String,
    var message: String
)