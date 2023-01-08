package com.potus.potus_front.websocket.socketclient

import com.potus.potus_front.websocket.CloseHandler
import okhttp3.Response
import okhttp3.WebSocket


class ChatListener(var room: String) :
    SpringBootWebSocketClient() {
    override fun onOpen(webSocket: WebSocket, response: Response?) {
        super.onOpen(webSocket, response)
        sendConnectMessage(webSocket)
        for (topic in topics.keys) {
            sendSubscribeMessage(webSocket, topic)
        }
        //turn the User with givenID's status On
        println("start listening Chat ")
        sendMessage(webSocket, "/app/chatroom/$room", "")
        closeHandler = CloseHandler(webSocket)
    }
}