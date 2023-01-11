package com.potus.potus_front.API.response.data_models

import com.potus.potus_front.API.response.NewGardenResponse
import java.util.*

data class UserGardenInfo(
    var createdDate: Date,
    var garden: NewGardenResponse,
    var role: String
)