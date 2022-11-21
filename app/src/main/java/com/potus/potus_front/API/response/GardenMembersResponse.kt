package com.potus.potus_front.API.response

import com.potus.potus_front.API.response.data_models.SimplifiedGardenMember

data class GardenMembersResponse(
    var members: List<SimplifiedGardenMember>
)