package com.potus.potus_front.websocket.model

class ChatMessage(val sender : String,  val message : String = "", val type : String,  val date : String ) {


   // MESSAGE JOIN LEAVE = TYPE
   override fun toString(): String {
       return "Chat{" +
               "sender='" + sender + '\'' +
               ", message='" + message + '\'' +
               ", type='" + type + '\'' +
               ", date='" + date + '\'' +
               '}'
   }
}