package com.potus.potus_front.websocket

import okhttp3.WebSocket

class CloseHandler(val webSocket: WebSocket) {

    fun close(){
        webSocket.close(1000,"close websocket");
    }

}