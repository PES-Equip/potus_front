package com.potus.potus_front.API.response.data_models

data class GasRegistry(
    var dangerLevel: String,
    var name: String,
    var unit: String,
    var value: Double
)