package com.potus.potus_front.websocket.socketclient

import com.potus.potus_front.websocket.CloseHandler
import okhttp3.Response
import okhttp3.WebSocket


class ChatDeliver(
    private val room: String,
    private val content: String
) :
    SpringBootWebSocketClient() {
    override fun onOpen(webSocket: WebSocket, response: Response?) {
        super.onOpen(webSocket, response)
        sendConnectMessage(webSocket)
        for (topic in topics.keys) {
            sendSubscribeMessage(webSocket, topic)
        }
        val destination = "/app/message/$room"
        sendMessage(webSocket, destination, content)
        closeHandler = CloseHandler(webSocket)
    }
}