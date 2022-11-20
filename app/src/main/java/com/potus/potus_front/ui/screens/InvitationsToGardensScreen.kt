package com.potus.potus_front.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.GardenRequest
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.RoseRed
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Preview
@Composable
fun InvitationsToGardensScreen() {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getInvitationList(
                "Bearer " + tokenState.token,
                "gardens/profile/requests"
            )

        if (call.isSuccessful) {
            call.body()?.let { tokenState.myInvitations(it.gardens) }
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
            InvitationsList(tokenState.invitations)
            //InvitationsList(listOf(Triple("We are testing things. Don't worry!", 100000, "TESTS")))
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_seleccio_jardi), painterResource(id = R.drawable.basic), painterResource(id = R.drawable.icona_nou_jardi))
    }
}

@Composable
fun InvitationsList (gardens: List<Triple<String, Int, String>>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gardens.size) {
                arrayItem -> InvitationItem(garden = gardens[arrayItem])
        }
    }
}

@Composable
fun InvitationItem(garden: Triple<String, Int, String>) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    var joinedGarden = remember { mutableStateOf(Triple("You do not have any pending invitations.", 0, "NO INVITATIONS")) }
    var toggled by remember { mutableStateOf(false) }

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
        if (!toggled) {
            Row(modifier = Modifier.align(Alignment.Start)) {
                Image(
                    painter = painterResource(id = R.drawable.icona_invitacio_tancada), "",
                    modifier = Modifier
                        .size(64.dp)
                        .align(CenterVertically)
                        .padding(start = 8.dp)
                )
                Text(
                    text = garden.third,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BraveGreen,
                    modifier = Modifier.padding(start = 8.dp).align(CenterVertically)
                )
            }
        }
        else {
            Column() {
                Row(modifier = Modifier.align(Alignment.Start)) {
                    Image(
                        painter = painterResource(id = R.drawable.icona_invitacio_oberta), "",
                        modifier = Modifier
                            .size(64.dp)
                            .align(CenterVertically)
                            .padding(start = 8.dp)
                    )
                    Text(
                        text = "\n" + garden.third + "\n\nMembers: " + garden.second.toString() + "\nAbout: " + garden.first + "\n",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp).align(CenterVertically)
                    )
                }
                Row(modifier = Modifier.align(CenterHorizontally)) {
                    Surface(
                        color = BraveGreen,
                        modifier = Modifier
                            .clickable(onClick = {
                                val askedGardenName = garden.third
                                CoroutineScope(Dispatchers.IO).launch {
                                    val gardenRequest = GardenRequest(name = askedGardenName)
                                    val call = getRetrofit()
                                        .create(APIService::class.java)
                                        .joinGarden(
                                            "Bearer " + tokenState.token,
                                            "gardens/profile/requests/$askedGardenName",
                                            gardenRequest
                                        )

                                        if (call.isSuccessful) {
                                            call.body()?.let { joinedGarden.value = it.garden.garden }
                                        } else {
                                            //ERROR MESSAGES, IF ANY (OpenDialog not present because error messages have been changed)
                                            error.value = call.code()
                                            openDialog.value = true
                                        }
                                }

                                /* SWITCHER: to the Garden */
                            })
                            .padding(8.dp)
                            .width(184.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = "Accept Invitation!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Daffodil,
                            textAlign = TextAlign.Center
                        )
                    }
                    Surface(
                        color = RoseRed,
                        modifier = Modifier
                            .clickable(onClick = {
                                /* ACTION CONFIRMATION POP-UP? */

                                val askedGardenName = garden.third
                                CoroutineScope(Dispatchers.IO).launch {
                                val gardenRequest = GardenRequest(name = askedGardenName)
                                val call = getRetrofit()
                                    .create(APIService::class.java)
                                    .refuseToJoinGarden(
                                        "Bearer " + tokenState.token,
                                        "gardens/profile/requests/$askedGardenName",
                                        gardenRequest
                                    )
                            }

                                /* SWITCHER: to itself (InvitationsToGardenScreen) */
                            })
                            .padding(8.dp)
                            .width(184.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = "Reject...",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Daffodil,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}