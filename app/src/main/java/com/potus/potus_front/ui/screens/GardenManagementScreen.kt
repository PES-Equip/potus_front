package com.potus.potus_front.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.GardenDescriptionRequest
import com.potus.potus_front.API.requests.GardenInvitationRequest
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random


@Preview
@Composable
fun GardenManagementScreen() {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    //val tokenState = TokenState.current
    //val user = tokenState.user!!
    val members = remember { mutableStateOf(listOf(Pair("THERE ARE NO USERS IN THIS GARDEN", "OWNER"))) }
    val description = remember { mutableStateOf(TextFieldValue()) }
    val invitedUser = remember { mutableStateOf(TextFieldValue()) }

    /*LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getGardenMembers(
                "Bearer " + tokenState.token,
                "garden/profile/members"
            )

        if (call.isSuccessful) {
            call.body()?.let { members.value = it.members }
        } else {
            //ERROR MESSAGES, IF ANY
            error.value = call.code()
            openDialog.value = true
        }
    }*/

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = 100, //user.potus.waterLevel,
            collection = 100, //user.currency,
            username = "JoeBiden", //user.username,
            addedWater = 0,
            addedLeaves = 0
        )
        Column(modifier = Modifier.weight(1f).background(Daffodil)) {
            Spacer(modifier = Modifier.size(8.dp))
            Column(modifier = Modifier.align(Alignment.Start).padding(8.dp).clip(RoundedCornerShape(10.dp)).background(SoothingGreen).fillMaxWidth()) {
                Text(
                    text = "MY GARDEN",
                    //text = tokenState.user?.garden_info?.garden?.third.toString(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                Row(modifier = Modifier.align(Alignment.Start).padding(start = 16.dp, end = 8.dp)) {
                    Text(
                        text = "Members: ",
                        fontSize = 20.sp,
                        color = BraveGreen,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "0",
                        //text = "Members: " + tokenState.user?.garden_info?.garden?.second.toString(),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(modifier = Modifier.align(Alignment.Start).padding(start = 16.dp, end = 8.dp)) {
                    Text(
                        text = "About: ",
                        fontSize = 20.sp,
                        color = BraveGreen,
                        textAlign = TextAlign.Center
                    )
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text(
                            text = "Test description."
                            //text = "Members: " + tokenState.user?.garden_info?.garden?.second.toString(),
                        ) },
                        modifier = Modifier
                            .width(296.dp)
                            .height(128.dp)
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                            .align(CenterVertically)
                    )
                }
                Button(
                    onClick = {
                        /*CoroutineScope(Dispatchers.IO).launch {
                            val newGardenDescriptionRequest = GardenDescriptionRequest(description = description.value.text)
                            val call = getRetrofit()
                                .create(APIService::class.java)
                                .changeGardenDescription(
                                    "Bearer " + tokenState.token,
                                    "gardens/profile",
                                    newGardenDescriptionRequest
                                )

                            if (call.isSuccessful) {
                                call.body()?.let { tokenState.user?.garden_info?.garden = it.garden }
                            } else {
                                //ERROR MESSAGES, IF ANY (OpenDialog not present because error messages have been changed)
                                error.value = call.code()
                                openDialog.value = true
                            }
                        }*/
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(CenterHorizontally),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Change description", color = Daffodil)
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(modifier = Modifier.align(Alignment.Start).padding(8.dp).clip(RoundedCornerShape(10.dp)).background(BraveGreen).fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.icona_convidar_jardi), "",
                    modifier = Modifier
                        .size(64.dp)
                        .align(CenterVertically)
                        .padding(start = 8.dp)
                )
                OutlinedTextField(
                    value = invitedUser.value,
                    onValueChange = { invitedUser.value = it },
                    label = { Text(text = "Invite User") },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .align(CenterVertically)
                )
                Button(
                    onClick = {
                        /*CoroutineScope(Dispatchers.IO).launch {
                            val garden = tokenState.user?.garden_info?.garden?.third.toString()
                            val receiver = invitedUser.value.toString()
                            val sendGardenInvitation = GardenInvitationRequest(garden = garden, user = receiver)
                            val call = getRetrofit().create(APIService::class.java)
                                .sendGardenInvitation(
                                "Bearer " + tokenState.token,
                                "gardens/$garden/requests/$receiver",
                                sendGardenInvitation
                            )
                        }*/
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = SoothingGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(CenterVertically),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Send!", color = Daffodil)
                }
            }
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_invitacions_jardins), painterResource(id = R.drawable.basic), painterResource(id = R.drawable.icona_jardi))
    }
}