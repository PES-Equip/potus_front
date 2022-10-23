package com.potus.potus_front.API

import java.util.*

data class PotusResponse(

    var health: Int,
    var waterLevel: Int,
    var createdDate: Date,
    var lastModified: Date,
    var alive: Boolean,
    var infested: Boolean,
    var actions: Map<String,ActionResponse>,
)