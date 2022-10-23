package com.potus.potus_front.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
class PostRequest (
    val body: String,
    val title: String,
    val userId: Int
)