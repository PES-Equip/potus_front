package com.potus.potus_front.websocket

class GardenMessageListener : StompMessageListener {

    override fun onMessage(message: StompMessage) {
        if(message.getContent() == "[]")
            return

        val stompMessageSerializer = StompMessageSerializer()

    }
}