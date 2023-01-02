package com.potus.potus_front.API.response.data_models

import org.jetbrains.annotations.NotNull

data class SimplifiedGardenMember(
    @NotNull
    var username: String,
    var role: String
)