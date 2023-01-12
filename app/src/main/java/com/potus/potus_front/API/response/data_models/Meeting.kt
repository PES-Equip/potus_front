package com.potus.potus_front.API.response.data_models

import com.potus.potus_front.API.response.GasesResponse
import java.util.*

data class Meeting(
    var address: String,
    var city: String,
    var end_date: Date,
    var id: Long,
    var region: GasesResponse,
    var start_date: Date,
    var subtitle: String,
    var title: String
)