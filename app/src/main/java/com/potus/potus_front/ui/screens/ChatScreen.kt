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
fun ChatScreen(onNavigateToProfile: () -> Unit, onNavigateToGarden: () -> Unit, onNavigateToSelection: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!
    var waterLevelState by remember { mutableStateOf(user.potus.waterLevel) }
    var collection by remember { mutableStateOf(user.currency) }
    var addedWater by remember { mutableStateOf(0) }
    var addedLeaves by remember { mutableStateOf(0) }
    var plantState by remember { mutableStateOf("DEFAULT") }
    // test -> user garden id room = garden id
    val room = "test"
    var chatListener: ChatListener = ChatListener(user.username)
    var topicHandler: TopicHandler = chatListener.subscribe("/chatroom/$room")

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

    //var thematicEvent by remember { mutableStateOf("DEFAULT") }

    LaunchedEffect(Dispatchers.IO) {
        val newUpdateStateRequest = InformLocationRequest(latitude = tokenState.location.first, length = tokenState.location.second)
        val call = getRetrofit()
            .create(APIService::class.java)
            .informLocation(
                "Bearer " + tokenState.token,
                "potus/events",
                newUpdateStateRequest
            )

        if (call.isSuccessful) {
            tokenState.myPotus(call.body())
            tokenState.user?.potus?.let { plantState = it.state }
        } else {
            //ERROR MESSAGES, IF ANY
            error.value = call.code()
            openDialog.value = true
        }
    }

    Column(Modifier.background(color = SoothingGreen)) {
        TopBar(
            waterLevel = waterLevelState,
            collection = collection,
            username = "CHAT",
            addedWater = addedWater,
            addedLeaves = addedLeaves,
            onNavigateToProfile = { onNavigateToProfile() }
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

    }
}