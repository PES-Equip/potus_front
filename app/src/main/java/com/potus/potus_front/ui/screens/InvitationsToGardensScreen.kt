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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.response.NewGardenResponse
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
fun InvitationsToGardensScreen(onNavigateToProfile: () -> Unit, onNavigateToGarden: () -> Unit, onNavigateToInvitations: () -> Unit, onNavigateToSelection: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToCreation: () -> Unit, onNavigateToShop: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!.user

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getInvitationList(
                "Bearer " + tokenState.token,
                "gardens/profile/requests"
            )

        if (call.isSuccessful) {
            call.body()?.let { tokenState.myInvitations(it) }
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
            addedLeaves = 0,
            onNavigateToProfile = { onNavigateToProfile() },
            onNavigateToShop = { onNavigateToShop()}
        )
        Column(modifier = Modifier.weight(1f).background(Daffodil)) {
            InvitationsList(tokenState.invitations, onNavigateToGarden, onNavigateToInvitations)
            //InvitationsList(listOf(NewGardenResponse("Christmas gang :)", 100000, "Fum, fum, fum"), NewGardenResponse("Developer's corner", 100000, "So tired..."), NewGardenResponse("Bosc", 100000, "Els originals!")), onNavigateToGarden, onNavigateToInvitations)
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_seleccio_jardi), onNavigateToSelection, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_nou_jardi), onNavigateToCreation)
    }
}

@Composable
fun InvitationsList (invitations: List<NewGardenResponse>, onNavigateToGarden: () -> Unit, onNavigateToInvitations: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(invitations.size) {
                arrayItem -> InvitationItem(invitation = invitations[arrayItem], onNavigateToGarden, onNavigateToInvitations)
        }
    }
}

@Composable
fun InvitationItem(invitation: NewGardenResponse, onNavigateToGarden: () -> Unit, onNavigateToInvitations: () -> Unit) {
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
                    text = invitation.name,
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
                        text = "\n" + invitation.name + "\n\nMembers: " + invitation.members_num.toString() + "\nAbout: " + invitation.description + "\n",
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
                                val askedGardenName = invitation.name
                                CoroutineScope(Dispatchers.IO).launch {
                                    val call = getRetrofit()
                                        .create(APIService::class.java)
                                        .joinGarden(
                                            "Bearer " + tokenState.token,
                                            "gardens/profile/requests/$askedGardenName",
                                            garden = askedGardenName
                                        )

                                    if (call.isSuccessful) {
                                        call.body()?.let { joinedGarden.value = Triple(it.garden.name, it.garden.members_num, it.garden.description) }
                                    } else {
                                        //ERROR MESSAGES, IF ANY (OpenDialog not present because error messages have been changed)
                                        error.value = call.code()
                                        openDialog.value = true
                                    }
                                }

                                CoroutineScope(Dispatchers.IO).launch {
                                    val call = getRetrofit().create(APIService::class.java)
                                        .getUser(
                                            "Bearer " + tokenState.token,
                                            "user/profile")

                                    if (call.isSuccessful) {
                                        tokenState.signUser(call.body())
                                    }
                                }

                                onNavigateToGarden()
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

                                val askedGardenName = invitation.name
                                CoroutineScope(Dispatchers.IO).launch {
                                val call = getRetrofit()
                                    .create(APIService::class.java)
                                    .refuseToJoinGarden(
                                        "Bearer " + tokenState.token,
                                        "gardens/profile/requests/$askedGardenName",
                                        garden = askedGardenName
                                    )
                            }

                                onNavigateToInvitations()
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