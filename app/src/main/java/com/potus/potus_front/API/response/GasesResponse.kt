package com.potus.potus_front.API.response

import com.potus.potus_front.API.response.data_models.GasRegistry

data class GasesResponse(
    var code: String,
    var latitude: Double,
    var length: Double,
    var name: String,
    var registry: Map<String, GasRegistry>
)