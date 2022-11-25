package com.potus.potus_front.API.response

import java.util.*

data class PotusResponse(
    var name: String,
    var health: Int,
    var waterLevel: Int,
    var createdDate: Date,
    var lastModified: Date,
    var alive: Boolean,
    var infested: Boolean,
    var actions: Map<String, ActionResponse>,
    var state: String
)