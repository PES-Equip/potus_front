package com.potus.potus_front.API.requests

import org.jetbrains.annotations.NotNull

data class GardenInvitationRequest(
    @NotNull()
    var garden: String,
    @NotNull()
    var user: String
)