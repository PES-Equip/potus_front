package com.potus.potus_front.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.InformLocationRequest
import com.potus.potus_front.R
import com.potus.potus_front.composables.GardenBottomBar
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.SoothingGreen
import com.potus.potus_front.websocket.StompMessage
import com.potus.potus_front.websocket.StompMessageListener
import com.potus.potus_front.websocket.StompMessageSerializer
import com.potus.potus_front.websocket.TopicHandler
import com.potus.potus_front.websocket.model.ChatMessage
import com.potus.potus_front.websocket.socketclient.ChatDeliver
import com.potus.potus_front.websocket.socketclient.ChatListener
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject


@Composable
fun ChatScreen(onNavigateToProfile: () -> Unit, onNavigateToShop: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToGarden: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    var actionString = remember { mutableStateOf("")  }

    val tokenState = TokenState.current
    val user = tokenState.user!!.user

    // test -> user garden id room = garden id
    val room = "test"
    var chatListener: ChatListener = ChatListener(user.username)
    val topicHandler: TopicHandler = chatListener.subscribe("/chatroom/$room")

    StompMessageSerializer.joinChat(user.username, room)

    chatListener.connect(StompMessageSerializer.url)
    val chats = mutableMapOf<String,ChatMessage>()
    val sml : StompMessageListener = object : StompMessageListener {
        override fun onMessage(message: StompMessage) {

            StompMessageSerializer.handleMessage(message,chats)

            println("------")
            println(chats)
            println("------")
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
            onNavigateToProfile = { onNavigateToProfile() },
            onNavigateToShop = { onNavigateToShop()}
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chats.size) { arrayItem ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .defaultMinSize(minHeight = 64.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoothingGreen)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.align(Alignment.Start)) {
                        Image(
                            painter = painterResource(id = com.potus.potus_front.R.drawable.icona_invitacio_tancada),
                            "",
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                        )
                        Text(
                            text = arrayItem.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BraveGreen,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }}
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    StompMessageSerializer.sendMessage("YEPA", user.username, room)
                },
            ) {
                Text("SEND")
            }
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_mercat), onNavigateToShop, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_jardi), onNavigateToGarden)
    }
}