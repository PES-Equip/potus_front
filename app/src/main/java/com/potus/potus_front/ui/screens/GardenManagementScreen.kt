package com.potus.potus_front.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.GardenDescriptionRequest
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.RoseRed
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun GardenManagementScreen(onNavigateToProfile: () -> Unit, onNavigateToPetitions: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToGarden: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!
    val description = remember { mutableStateOf(TextFieldValue()) }
    val invitedUser = remember { mutableStateOf(TextFieldValue()) }

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = user.potus.waterLevel,
            collection = user.currency,
            username = user.username,
            addedWater = 0,
            addedLeaves = 0,
            onNavigateToProfile = { onNavigateToProfile() }
        )
        Column(modifier = Modifier.weight(1f).background(Daffodil)) {
            Spacer(modifier = Modifier.size(8.dp))
            LazyColumn(
                modifier = Modifier.align(Alignment.Start).padding(8.dp).clip(RoundedCornerShape(10.dp)).background(SoothingGreen).fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        //text = "MY GARDEN",
                        text = tokenState.user?.garden_info?.garden?.name.toString(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    Row(
                        modifier = Modifier.align(Alignment.Start)
                            .padding(start = 16.dp, end = 8.dp)
                    ) {
                        Text(
                            text = "Members: ",
                            fontSize = 20.sp,
                            color = BraveGreen,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            //text = "0",
                            text = tokenState.user?.garden_info?.garden?.members_num.toString(),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.Start)
                            .padding(start = 16.dp, end = 8.dp)
                    ) {
                        Text(
                            text = "About: ",
                            fontSize = 20.sp,
                            color = BraveGreen,
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = description.value,
                            onValueChange = { description.value = it },
                            label = {
                                Text(
                                    //text = "Test description."
                                    text = tokenState.user?.garden_info?.garden?.description.toString(),
                                )
                            },
                            modifier = Modifier
                                .width(296.dp)
                                .height(128.dp)
                                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                                .align(CenterVertically)
                        )
                    }
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val newGardenDescriptionRequest =
                                    GardenDescriptionRequest(description = description.value.text)
                                val call = getRetrofit()
                                    .create(APIService::class.java)
                                    .changeGardenDescription(
                                        "Bearer " + tokenState.token,
                                        "gardens/profile",
                                        newGardenDescriptionRequest
                                    )

                                if (call.isSuccessful) {
                                    call.body()?.let {
                                        tokenState.user?.garden_info?.garden?.description =
                                            it.description
                                    }
                                } else {
                                    //ERROR MESSAGES, IF ANY (OpenDialog not present because error messages have been changed)
                                    error.value = call.code()
                                    openDialog.value = true
                                }
                            }
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
            //Spacer(modifier = Modifier.size(8.dp))
                item {
                    Row(
                        modifier = Modifier.align(Alignment.Start).padding(8.dp)
                            .clip(RoundedCornerShape(10.dp)).background(BraveGreen).fillMaxWidth()
                    ) {
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
                                CoroutineScope(Dispatchers.IO).launch {
                                    val garden =
                                        tokenState.user?.garden_info?.garden?.name.toString()
                                    val receiver = invitedUser.value.toString()
                                    getRetrofit().create(APIService::class.java)
                                        .sendGardenInvitation(
                                            "Bearer " + tokenState.token,
                                            "gardens/$garden/requests/$receiver",
                                            garden = garden,
                                            user = receiver
                                        )
                                }
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
            //Spacer(modifier = Modifier.size(64.dp))
                item {
                    Button(
                        onClick = {
                            /* SHOULD ASK FOR CONFIRMATION THROUGH A POP-UP */

                            CoroutineScope(Dispatchers.IO).launch {
                                getRetrofit()
                                    .create(APIService::class.java)
                                    .exitGarden(
                                        "Bearer " + tokenState.token,
                                        "gardens/profile"
                                    )
                            }

                            onNavigateToHome()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = RoseRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .align(CenterHorizontally),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = "Leave Garden", color = Daffodil)
                    }
                }
                item {
                    Button(
                        onClick = {
                            /* SHOULD ASK FOR CONFIRMATION THROUGH A POP-UP */

                            val askedGardenName =
                                tokenState.user?.garden_info?.garden?.name.toString()
                            CoroutineScope(Dispatchers.IO).launch {
                                getRetrofit()
                                    .create(APIService::class.java)
                                    .removeGarden(
                                        "Bearer " + tokenState.token,
                                        "gardens/$askedGardenName",
                                        garden = askedGardenName
                                    )
                            }

                            onNavigateToHome()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .align(CenterHorizontally),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = "DELETE GARDEN", color = Daffodil)
                    }
                }
            }
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_peticions_jardi), onNavigateToPetitions, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_jardi), onNavigateToGarden)
    }
}