package com.potus.potus_front.websocket.model

class Chat(var id: String, var sender: String, var receiver: String, var content: String) {

    override fun toString(): String {
        return "Chat{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                '}'
    }
}