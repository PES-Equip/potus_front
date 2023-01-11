package com.potus.potus_front.API.response

import com.potus.potus_front.API.response.data_models.User
import java.util.Date

data class ChatResponse(
    var date: Date,
    var id: String,
    var message: String,
    var room: String,
    var sender: User,
    var status: String
)