package com.potus.potus_front.API.requests

import org.jetbrains.annotations.NotNull

data class CreateGardenRequest(
    @NotNull()
    var name: String
)