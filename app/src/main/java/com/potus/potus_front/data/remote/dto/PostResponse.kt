package com.potus.potus_front.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
class PostResponse (
    val body: String,
    val title: String,
    val id: Int,
    val userId: Int
)