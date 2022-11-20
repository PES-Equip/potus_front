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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
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
fun GardenScreen() {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!
    val members = remember { mutableStateOf(listOf(Pair("THERE ARE NO USERS IN THIS GARDEN", "OWNER"))) }
    val invitedUser = remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(Dispatchers.IO) {
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
    }

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = user.potus.waterLevel,
            collection = user.currency,
            username = user.username,
            addedWater = 0,
            addedLeaves = 0
        )
        Column(modifier = Modifier.weight(1f).background(Daffodil)) {
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
                        CoroutineScope(Dispatchers.IO).launch {
                            val garden = tokenState.user?.garden_info?.garden?.third.toString()
                            val receiver = invitedUser.value.toString()
                            val sendGardenInvitation = GardenInvitationRequest(garden = garden, user = receiver)
                            val call = getRetrofit().create(APIService::class.java)
                                .sendGardenInvitation(
                                "Bearer " + tokenState.token,
                                "gardens/$garden/requests/$receiver",
                                sendGardenInvitation
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
            //MembersList(listOf(Pair("Me", "OWNER"), Pair("You", "ADMIN"), Pair("He", "ADMIN"), Pair("She", "ADMIN"), Pair("They", "ADMIN")))
            MembersList(members.value)
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_mercat), painterResource(id = R.drawable.basic), painterResource(id = R.drawable.icona_xat))
    }
}

@Composable
fun MembersList (members: List<Pair<String, String>>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(members.size) {
                arrayItem -> MemberItem(member = members[arrayItem])
        }
    }
}

@Composable
fun MemberItem(member: Pair<String, String>) {
    var gases = arrayOf("C6H6", "Cl2", "CO", "H2S", "HCl", "HCNM", "HCT", "Hg", "NO2", "NO", "NOX", "O3", "PM1", "PM2_5", "PM10", "PS", "SO2")
    var toggled by remember { mutableStateOf(false) }
    var randomAvatar = gases[Random.nextInt(gases.size)]

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .defaultMinSize(minHeight = 64.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SoothingGreen)
            .toggleable(value = toggled, onValueChange = { toggled = it })
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .align(CenterVertically)
                    .padding(start = 8.dp)
            ) { CenterArea(randomAvatar) }
            if (!toggled) {
                Text(
                    text = member.first,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BraveGreen,
                    modifier = Modifier.padding(start = 8.dp).align(CenterVertically)
                )
            }
            else {
                Surface (modifier = Modifier.fillMaxWidth(), color = Color.Transparent) {
                    Text(
                        text = "\n" + member.first + "\n\nStatus: " + member.second + "\n",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp).align(CenterVertically)
                    )
                }
            }
        }
    }
}