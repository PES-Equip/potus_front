package com.potus.potus_front.API.response.data_models

import android.media.tv.TvContract.WatchNextPrograms
import com.potus.potus_front.API.response.PotusResponse

data class Shop(
    var current: Integer,
    var dates: List<String>,
    var level: Int,
    var next: Boolean,
    var next_levels: Integer,
    var trophy: Trophy,
    var updatedDate: String,
    var upgraded: Boolean
)