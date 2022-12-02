package com.potus.potus_front.websocket

interface StompMessageListener {

    fun onMessage(message: StompMessage)
}