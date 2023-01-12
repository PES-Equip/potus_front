package com.potus.potus_front.API.response

import com.potus.potus_front.API.response.data_models.User
import java.util.Date

data class ReportResponse(
    var date: Date,
    var id: String,
    var message: ChatResponse,
    var sender: User
)