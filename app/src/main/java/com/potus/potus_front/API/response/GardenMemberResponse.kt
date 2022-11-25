package com.potus.potus_front.API.response

import com.potus.potus_front.API.response.data_models.GardenUser

data class GardenMemberResponse(
    var garden: NewGardenResponse,
    var role: String,
    var user: GardenUser
)