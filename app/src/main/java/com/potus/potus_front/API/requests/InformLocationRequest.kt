package com.potus.potus_front.API.requests

import org.jetbrains.annotations.NotNull

data class InformLocationRequest(
    @NotNull()
    var latitude: Int,
    @NotNull()
    var length: Int
)