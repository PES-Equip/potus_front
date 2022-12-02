package com.potus.potus_front.websocket.socketclient

import com.potus.potus_front.websocket.CloseHandler
import com.potus.potus_front.websocket.StompMessage
import com.potus.potus_front.websocket.StompMessageSerializer
import com.potus.potus_front.websocket.TopicHandler
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit


open class SpringBootWebSocketClient : WebSocketListener {
    var topics: MutableMap<String?, TopicHandler> = HashMap()
    var closeHandler: CloseHandler? = null
    var id = "sub-001"
    var webSocket: WebSocket? = null

    constructor() {}
    constructor(id: String) {
        this.id = id
    }

    fun subscribe(topic: String?): TopicHandler {
        val handler = TopicHandler(topic)
        topics[topic] = handler
        return handler
    }

    fun unSubscribe(topic: String?) {
        topics.remove(topic)
    }

    fun getTopicHandler(topic: String?): TopicHandler? {
        return if (topics.containsKey(topic)) {
            topics[topic]
        } else null
    }

    @JvmName("getWebSocket1")
    fun getWebSocket(): WebSocket? {
        return webSocket
    }

    fun connect(address: String?) {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
        val request: Request = Request.Builder()
            .url(address)
            .build()
        client.newWebSocket(request, this)

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown()
    }

    override fun onOpen(webSocket: WebSocket, response: Response?) {
        this.webSocket = webSocket
        sendConnectMessage(webSocket)
        for (topic in topics.keys) {
            sendSubscribeMessage(webSocket, topic)
        }
        closeHandler = CloseHandler(webSocket)
    }

    fun sendConnectMessage(webSocket: WebSocket) {
        val message = StompMessage("CONNECT")
        message.put("accept-version", "1.1")
        message.put("heart-beat", "10000,10000")
        webSocket.send(StompMessageSerializer.serialize(message))
    }

    fun sendSubscribeMessage(webSocket: WebSocket, topic: String?) {
        val message = StompMessage("SUBSCRIBE")
        message.put("id", id)
        message.put("destination", topic!!)
        webSocket.send(StompMessageSerializer.serialize(message))
    }

    fun disconnect() {
        if (webSocket != null) {
            closeHandler!!.close()
            webSocket = null
            closeHandler = null
        }
    }

    val isConnected: Boolean
        get() = closeHandler != null

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        val message: StompMessage? = text?.let { StompMessageSerializer.deserialize(it) }
        val topic = message?.getHeader("destination")
        if (topics.containsKey(topic)) {
            topics[topic]!!.onMessage(message)
        }
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        //System.out.println("MESSAGE: " + bytes.hex());
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("CLOSE: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    fun sendMessage(webSocket: WebSocket, topic: String?, content: String?) {
        val message = StompMessage("SEND")
        message.put("destination", topic!!)
        message.setContent(content!!)
        val serializedMessage: String = StompMessageSerializer.serialize(message)
        webSocket.send(serializedMessage)
    }
}