package com.potus.potus_front.API.response

import com.potus.potus_front.API.response.data_models.User
import com.potus.potus_front.API.response.data_models.UserTrophy

data class UserResponse(
    var user: User,
    var trophies: List<UserTrophy>
)