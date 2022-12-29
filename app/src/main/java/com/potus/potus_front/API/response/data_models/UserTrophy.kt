package com.potus.potus_front.API.response.data_models

import com.potus.potus_front.API.response.UserResponse
import java.util.*
import kotlin.collections.ArrayList

data class UserTrophy(
    var trophy: Trophy,
    var user: UserResponse,
    var level: Int,
    var current: Int,
    var levelDates: ArrayList<Date>,
    var updateDate: Date,
    var upgraded: Boolean
)