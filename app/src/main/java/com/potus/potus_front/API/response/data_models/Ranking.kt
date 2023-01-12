package com.potus.potus_front.API.response.data_models

import androidx.annotation.Nullable
import com.potus.potus_front.API.response.PotusResponse

data class Ranking(
    var id: Long,
    var username: String,
    var level: Int,
    var current: Int
)