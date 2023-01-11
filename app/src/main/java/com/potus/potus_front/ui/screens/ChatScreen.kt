package com.potus.potus_front.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.response.ChatResponse
import com.potus.potus_front.R
import com.potus.potus_front.composables.GardenBottomBar
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import com.potus.potus_front.websocket.StompMessage
import com.potus.potus_front.websocket.StompMessageListener
import com.potus.potus_front.websocket.StompMessageSerializer
import com.potus.potus_front.websocket.TopicHandler
import com.potus.potus_front.websocket.model.ChatMessage
import com.potus.potus_front.websocket.socketclient.ChatListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ChatScreen(onNavigateToProfile: () -> Unit, onNavigateToShop: () -> Unit, onNavigateToMeetings: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToGarden: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val actionString = remember { mutableStateOf("")  }

    val tokenState = TokenState.current
    val user = tokenState.user!!.user
    val garden = user.garden_info?.garden?.name.toString()

    val room = user.garden_info?.garden?.id.toString() //"test"
    val chatListener = ChatListener(user.username)
    val topicHandler: TopicHandler = chatListener.subscribe("/chatroom/$room")

    val message = remember { mutableStateOf("")  }

    StompMessageSerializer.joinChat(user.username, room)

    chatListener.connect(StompMessageSerializer.url)
    val historicChat = remember { mutableStateOf<List<ChatResponse>>(listOf()) }

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getHistoricChat(
                "Bearer " + tokenState.token,
                "gardens/$garden/chats",
                garden = garden,
                page = 0
            )

        val eBody = call.errorBody()
        if (call.isSuccessful) {
            call.body()?.let { historicChat.value = it }
        } else {
            //ERROR MESSAGES, IF ANY
            openDialog.value = true
            if (eBody != null) {
                actionString.value = JSONObject(eBody.string()).getString("message")
            }
        }
    }

    val chats = mutableMapOf<String,ChatMessage>()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    var i = 0
    historicChat.value.forEach { chatResponse ->
        //if (chatResponse.message != "")
            chats += i.toString() to ChatMessage(sender = chatResponse.sender.username, message = chatResponse.message, "MESSAGE", date = dateFormat.format(chatResponse.date).toString())
        i += 1
    }

    val sml : StompMessageListener = object : StompMessageListener {
        override fun onMessage(message: StompMessage) {
            StompMessageSerializer.handleMessage(message,chats)
        }
    }
    topicHandler.addListener(sml)

    chatListener.connect(StompMessageSerializer.url)

    Column(Modifier.background(color = SoothingGreen)) {
        TopBar(
            waterLevel = user.potus.waterLevel,
            collection = user.currency,
            username = user.username,
            addedWater = 0,
            addedLeaves = 0,
            onNavigateToProfile = { onNavigateToProfile() }
        )
        Column (modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = message.value,
                    onValueChange = { message.value = it },
                    label = { Text(text = "New message...") },
                    modifier = Modifier
                        .weight(0.75f)
                        .padding(horizontal = 8.dp),
                    shape = MaterialTheme.shapes.medium
                )
                Button(
                    onClick = {
                        val letter = message.value
                        if (letter != "") {
                            StompMessageSerializer.sendMessage(
                                message = letter,
                                user.username,
                                room
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                val call = getRetrofit()
                                    .create(APIService::class.java)
                                    .sendChatMessage(
                                        "Bearer " + tokenState.token,
                                        "gardens/$garden/chat/$letter",
                                        garden = garden,
                                        message = letter
                                    )

                                val eBody = call.errorBody()
                                if (call.isSuccessful) {
                                    call.body()?.let { historicChat.value += it }
                                } else {
                                    //ERROR MESSAGES, IF ANY
                                    openDialog.value = true
                                    if (eBody != null) {
                                        actionString.value = JSONObject(eBody.string()).getString("message")
                                    }
                                }
                            }

                            chats += chats.size.toString() to ChatMessage(sender = user.username, message = message.value, "MESSAGE", Date().time.toString())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen),
                    modifier = Modifier
                        .weight(0.25f)
                        .height(80.dp)
                        .padding(8.dp)
                        .align(CenterVertically),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("SEND")
                }
            }

            val popUpContext = LocalContext.current

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
                //reverseLayout = true
            ) {
                items(chats.size) { chat ->
                    var toggled by remember { mutableStateOf(false) }
                    val message = chats[chat.toString()]?.message.toString()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .defaultMinSize(minHeight = 64.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Daffodil)
                            .toggleable(value = toggled, onValueChange = { toggled = it })
                            .animateContentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val invertedPosition = chats.size - chat - 1
                        Column (modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Row() {
                                Text(
                                    //text = chats[invertedPosition.toString()]?.sender.toString(),
                                    text = chats[chat.toString()]?.sender.toString(),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .padding(start = 8.dp, bottom = 8.dp)
                                        .align(CenterVertically)
                                )
                                Text(
                                    //text = chats[invertedPosition.toString()]?.date.toString(),
                                    text = chats[chat.toString()]?.date.toString(),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .padding(start = 8.dp, bottom = 8.dp)
                                        .align(CenterVertically)
                                )
                            }
                            Text(
                                //text = chats[invertedPosition.toString()]?.message.toString(),
                                text = message,
                                fontSize = 20.sp,
                                color = BraveGreen,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .align(Alignment.Start)
                            )
                            if (toggled) {
                                Button(
                                    onClick = {
                                        val builder = android.app.AlertDialog.Builder(popUpContext)
                                        builder.setTitle("REPORT USER")
                                        builder.setMessage("Are you sure you want to report this user?")
                                        builder.setPositiveButton("REPORT") { dialog, which ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                val call = getRetrofit()
                                                    .create(APIService::class.java)
                                                    .sendReport(
                                                        "Bearer " + tokenState.token,
                                                        "gardens/$garden/profile/report/$message",
                                                        garden = garden,
                                                        message = message
                                                    )

                                                val eBody = call.errorBody()
                                                if (!call.isSuccessful) {
                                                    //ERROR MESSAGES, IF ANY
                                                    openDialog.value = true
                                                    if (eBody != null) {
                                                        actionString.value = JSONObject(eBody.string()).getString("message")
                                                    }
                                                }
                                            }
                                        }
                                        builder.setNegativeButton("CANCEL") { dialog, which ->
                                            dialog.dismiss()
                                        }
                                        val dialog = builder.create()
                                        dialog.show()
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(text = "REPORT USER", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (openDialog.value) {
            Toast.makeText(LocalContext.current, actionString.value, Toast.LENGTH_SHORT).show()
            openDialog.value = false
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_meetings), onNavigateToMeetings, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_jardi), onNavigateToGarden)
    }
}