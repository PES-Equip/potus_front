package com.potus.potus_front.API.response

data class UserResponse(
    var username: String,
    var email: String,
    var currency: Int,
    var potus: PotusResponse,
    var status: String,
)