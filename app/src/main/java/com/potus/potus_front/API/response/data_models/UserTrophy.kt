package com.potus.potus_front.API.response.data_models

import com.potus.potus_front.API.response.UserResponse
import java.util.*
import kotlin.collections.ArrayList

data class UserTrophy(
    var current: Int,
    var dates: ArrayList<Date>,
    var level: Int,
    var next: Boolean,
    var next_level: Int,
    var trophy: Trophy,
    var updatedDate: Date,
    var upgraded: Boolean
)