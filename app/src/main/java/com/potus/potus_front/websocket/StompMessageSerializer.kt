package com.potus.potus_front.websocket

import com.potus.potus_front.websocket.model.Chat
import com.potus.potus_front.websocket.model.ChatMessage
import com.potus.potus_front.websocket.model.User
import com.potus.potus_front.websocket.socketclient.ChatDeliver
import okhttp3.internal.http.HttpDate.format
import org.json.JSONObject
import java.lang.String.format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StompMessageSerializer {

    companion object {


        const val url = "ws://10.0.2.2:8080/ws/websocket"

    fun serialize(message: StompMessage): String {

        val buffer: StringBuffer = StringBuffer()

        buffer.append(message.getCommand() + "\n")

        message.getHeaders().keys.forEach{ header:String ->
            buffer.append(header).append(":").append(message.getHeaders()[header]).append("\n")
        }

        buffer.append("\n")
        buffer.append(message.getContent())

        buffer.append('\u0000')

        return buffer.toString()
    }

    fun deserialize(message: String): StompMessage {
        val lines: List<String> = message.split("\n")

        val command : String = lines[0].trim()

        var result : StompMessage = StompMessage(command)

        var i = 1
        while (i < lines.size) {
            val line = lines[i].trim()
            if (line == "") {
                break
            }
            val parts = line.split(":").toTypedArray()
            val name = parts[0].trim { it <= ' ' }
            var value = ""
            if (parts.size == 2) {
                value = parts[1].trim { it <= ' ' }
            }
            result.put(name, value)
            ++i
        }

        val sb = StringBuilder()

        while (i < lines.size) {
            sb.append(lines[i])
            ++i
        }

        val body = sb.toString().trim { it <= ' ' }

        result.setContent(body)
        return result
    }

    fun putUserListStompMessageToListOfUsers(
        stompMessage: StompMessage,
        currentUser: User
    ): List<User>? {
        var stompMessageString = stompMessage.getContent()
        val userList: MutableList<User> = ArrayList()
        while (true) {
            val idStart = stompMessageString.indexOf(':')
            val idEnd = stompMessageString.indexOf(',')
            val id = stompMessageString.substring(idStart + 1, idEnd)
            stompMessageString = stompMessageString.substring(idEnd + 1).replace("\"", "")
            val userNameStart = stompMessageString.indexOf(':')
            val userNameEnd = stompMessageString.indexOf(',')
            val userName =
                stompMessageString.substring(userNameStart + 1, userNameEnd).replace("\"", "")
            stompMessageString = stompMessageString.substring(userNameEnd + 1)
            val genderStart = stompMessageString.indexOf(':')
            val genderEnd = stompMessageString.indexOf(',')
            val gender = stompMessageString.substring(genderStart + 1, genderEnd).replace("\"", "")
            stompMessageString = stompMessageString.substring(genderEnd + 1)
            val statusStart = stompMessageString.indexOf(':')
            val statusEnd = stompMessageString.indexOf(',')
            val status = stompMessageString.substring(statusStart + 1, statusEnd).replace("\"", "")
            stompMessageString = stompMessageString.substring(statusEnd + 1)
            val imageURLStart = stompMessageString.indexOf(':')
            val imageURLEnd = stompMessageString.indexOf('}')
            val imageURL =
                stompMessageString.substring(imageURLStart + 1, imageURLEnd).replace("\"", "")
            stompMessageString = stompMessageString.substring(imageURLEnd + 2)
            if (id != currentUser.id) {
                userList.add(User(id, userName, status, imageURL, gender))
            }
            if (stompMessageString == "") {
                break
            }
        }
        return userList
    }


    fun joinChat(username: String, room : String){
        val json: JSONObject = JSONObject()
        json.put("senderName", username)
        json.put("message", "")
        json.put("status", "JOIN")
        val chatDeliver: ChatDeliver = ChatDeliver(room, json.toString())
        chatDeliver.connect(url)
        chatDeliver.disconnect()
    }

    // room should be clan id
    fun leaveChat(username: String, room : String){
        val json: JSONObject = JSONObject()
        json.put("senderName", username)
        json.put("message", "")
        json.put("status", "LEAVE")
        val chatDeliver: ChatDeliver = ChatDeliver(room, json.toString())
        chatDeliver.connect(url)
        chatDeliver.disconnect()
    }

    // room should be clan id
    fun sendMessage(message: String, username: String, room : String){
        val json: JSONObject = JSONObject()
        json.put("senderName", username)
        json.put("message", message)
        json.put("status", "MESSAGE")
        val chatDeliver: ChatDeliver = ChatDeliver(room, json.toString())
        chatDeliver.connect(url)
        chatDeliver.disconnect()
    }

    fun handleMessage(message: StompMessage, chats: MutableMap<String, ChatMessage>, ids: MutableMap<String, String>){
        if(message.getContent() == "[]")
            return;

        val json : JSONObject = JSONObject(message.getContent())
        val type = json.get("status") as String
        val id = json.get("id") as String
        if(! chats.containsKey(id) && json.get("status").equals("MESSAGE")){
            var messageContent = ""
            if(! json.get("message").equals(null)) {
                messageContent = json.get("message").toString()
            }

            val normalDate = json.get("date").toString()

            //val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
            //val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            //val date = inputFormat.parse(json.get("date").toString())
            //val formattedDate = date?.let { dateFormat.format(it) }.toString()

            val result = ChatMessage(json.get("senderName").toString(), messageContent,json.get("status").toString(), normalDate)

            chats[id] = result
            ids[chats.size.toString()] = id
        }
    }

    fun putChatListStompMessageToListOfChats(stompMessage: StompMessage): List<Chat>? {
        var stompMessageString = stompMessage.getContent()
        val chatList: MutableList<Chat> = ArrayList()
        while (true) {
            val idStart = stompMessageString.indexOf(':')
            val idEnd = stompMessageString.indexOf(',')
            val id = stompMessageString.substring(idStart + 1, idEnd)
            stompMessageString = stompMessageString.substring(idEnd + 1).replace("\"", "")
            val senderStart = stompMessageString.indexOf(':')
            val senderEnd = stompMessageString.indexOf(',')
            val sender = stompMessageString.substring(senderStart + 1, senderEnd).replace("\"", "")
            stompMessageString = stompMessageString.substring(senderEnd + 1)
            val receiverStart = stompMessageString.indexOf(':')
            val receiverEnd = stompMessageString.indexOf(',')
            val receiver =
                stompMessageString.substring(receiverStart + 1, receiverEnd).replace("\"", "")
            stompMessageString = stompMessageString.substring(receiverEnd + 1)
            val contentStart = stompMessageString.indexOf(':')
            val contentEnd = stompMessageString.indexOf('}')
            val content =
                stompMessageString.substring(contentStart + 1, contentEnd).replace("\"", "")
            stompMessageString = stompMessageString.substring(contentEnd + 2)
            chatList.add(Chat(id, sender, receiver, content))
            if (stompMessageString == "") {
                break
            }
        }
        return chatList
    }
    }
}