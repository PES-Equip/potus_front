package com.potus.potus_front.API.response

data class GardenListResponse(
    var gardens: List<Triple<String, Int, String>>
)