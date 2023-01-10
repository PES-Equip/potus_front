package com.potus.potus_front.API.requests

import org.jetbrains.annotations.NotNull

data class ChangeMemberRoleRequest(

    @NotNull()
    var role: String
)