package com.potus.potus_front.API.response

data class NewGardenResponse(
    var id: Int,
    var name: String,
    var members_num: Int,
    var description: String
)