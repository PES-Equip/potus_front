package com.potus.potus_front.websocket

class StompMessage {

    private var headers: MutableMap<String,String> = HashMap();

    private var body: String = ""
    private var command: String = ""

    constructor(command: String){
        this.command = command
    }

    constructor(){

    }

    fun getHeader(name: String): String? {
        return headers[name]
    }

    fun put(name: String, value: String){
        headers[name] = value
    }

    fun setContent(body: String){
        this.body = body
    }

    fun getContent(): String {
        return body
    }

    fun getHeaders(): MutableMap<String, String> {
        return headers
    }

    fun getCommand() : String{
        return command
    }
}