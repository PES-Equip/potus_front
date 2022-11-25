package com.potus.potus_front.API.response

data class GardenResponse(
    var garden: NewGardenResponse,
    var role: String,
    var user: UserResponse
)