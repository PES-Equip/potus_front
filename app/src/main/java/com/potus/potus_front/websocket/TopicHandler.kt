package com.potus.potus_front.websocket

class TopicHandler {
    var topic: String? = null
        private set
    private val listeners: MutableSet<StompMessageListener> = HashSet()

    constructor(topic: String?) {
        this.topic = topic
    }

    constructor() {}

    fun addListener(listener: StompMessageListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: StompMessageListener) {
        listeners.remove(listener)
    }

    fun onMessage(message: StompMessage?) {
        for (listener in listeners) {
            listener.onMessage(message!!)
        }
    }
}