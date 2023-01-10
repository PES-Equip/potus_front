package com.potus.potus_front.websocket.model

import java.io.Serializable

class User : Serializable {
    var id: String? = null
    var username: String? = null
    var gender: String? = null
    var status: String? = null
    var imageURL: String?

    constructor(
        id: String?,
        username: String?,
        status: String?,
        imageURL: String?,
        gender: String?
    ) {
        this.id = id
        this.username = username
        this.status = status
        this.imageURL = imageURL
        this.gender = gender
    }

    constructor() {
        imageURL = null
    }

    override fun toString(): String {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}'
    }
}