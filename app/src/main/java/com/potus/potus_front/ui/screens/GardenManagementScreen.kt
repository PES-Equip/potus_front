package com.potus.potus_front.ui.screens

import android.app.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
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
import org.json.JSONObject
import timber.log.Timber


@Composable
fun GardenManagementScreen(onNavigateToProfile: () -> Unit, onNavigateToPetitions: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToGarden: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    var actionString = ""
    val popUpContext = LocalContext.current

    val tokenState = TokenState.current
    val user = tokenState.user!!.user
    val garden = tokenState.user?.user?.garden_info?.garden?.name.toString()
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
        Column(modifier = Modifier
            .weight(1f)
            .background(Daffodil)) {
            Spacer(modifier = Modifier.size(8.dp))
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(8.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        //text = "MY GARDEN",
                        text = garden,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.Start)
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
                            text = tokenState.user?.user?.garden_info?.garden?.members_num.toString(),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.Start)
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
                                    text = tokenState.user?.user?.garden_info?.garden?.description.toString(),
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
                                val newGardenDescriptionRequest = GardenDescriptionRequest(description = description.value.text)
                                val call = getRetrofit()
                                    .create(APIService::class.java)
                                    .changeGardenDescription(
                                        "Bearer " + tokenState.token,
                                        "gardens/profile",
                                        newGardenDescriptionRequest
                                    )

                                val eBody = call.errorBody()
                                if (call.isSuccessful) {
                                    call.body()?.let {
                                        tokenState.user?.user?.garden_info?.garden?.description = it.description
                                    }
                                } else {
                                    openDialog.value = true
                                    if (eBody != null) {
                                        actionString = JSONObject(eBody.string()).getString("message")
                                    }
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
                item {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BraveGreen)
                            .fillMaxWidth()
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
                                    val receiver = invitedUser.value.text
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
                item {
                    Button(
                        onClick = {
                            val builder = AlertDialog.Builder(popUpContext)
                            builder.setTitle("EXIT")
                            builder.setMessage("Are you sure you want to leave the Garden?")
                            builder.setPositiveButton("LEAVE") { dialog, which ->
                                user.garden_info = null

                                CoroutineScope(Dispatchers.IO).launch {
                                    getRetrofit()
                                        .create(APIService::class.java)
                                        .exitGarden(
                                            "Bearer " + tokenState.token,
                                            "gardens/profile"
                                        )
                                }

                                Timber.tag("DOES IT FUCKING WORK?").d("Succesfully ")

                                onNavigateToHome()
                            }
                            builder.setNegativeButton("STAY") { dialog, which ->
                                dialog.dismiss()
                            }
                            val dialog = builder.create()
                            dialog.show()
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
                            val builder = AlertDialog.Builder(popUpContext)
                            builder.setTitle("DELETE")
                            builder.setMessage("Are you sure you want to delete this Garden?")
                            builder.setPositiveButton("DELETE") { dialog, which ->
                                user.garden_info = null

                                CoroutineScope(Dispatchers.IO).launch {
                                    getRetrofit()
                                        .create(APIService::class.java)
                                        .removeGarden(
                                            "Bearer " + tokenState.token,
                                            "gardens/$garden",
                                            garden = garden
                                        )
                                }
                                onNavigateToHome()
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