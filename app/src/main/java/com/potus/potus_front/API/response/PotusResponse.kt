package com.potus.potus_front.API.response

import java.util.*

data class PotusResponse(
    var actions: Map<String, ActionResponse>,
    var alive: Boolean,
    var createdDate: Date,
    var currencyGenerators: List<Pair<String,Int>>,
    var currencyMultiplier: Int,
    var eventBonus: Int,
    var festivityBonus: Int,
    var health: Int,
    var ignored: Boolean,
    var infested: Boolean,
    var lastModified: Date,
    var name: String,
    var permanentBonus: Int,
    var pruningMaxCurrency: Int,
    var state: String,
    var waterLevel: Int,
    var waterRecovery: Int
)