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
import com.potus.potus_front.API.requests.GardenInvitationRequest
import com.potus.potus_front.API.response.*
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
fun PetitionsToGardensScreen(onNavigateToProfile: () -> Unit, onNavigateToPetitions: () -> Unit, onNavigateToManagement: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToGarden: () -> Unit) {
    val openDialog = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf(200) }

    val tokenState = TokenState.current
    val user = tokenState.user!!

    LaunchedEffect(Dispatchers.IO) {
        val garden = user.garden_info.garden.name
        val call = getRetrofit()
            .create(APIService::class.java)
            .getGardenPetitionList(
                "Bearer " + tokenState.token,
                "gardens/$garden/requests",
                garden = garden
            )

        if (call.isSuccessful) {
            call.body()?.let { tokenState.myPetitions(it) }
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
            onNavigateToProfile = { onNavigateToProfile() }
        )
        Column(modifier = Modifier.weight(1f).background(Daffodil)) {
            PetitionsList(tokenState.petitions, onNavigateToPetitions)
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_gestio_jardi), onNavigateToManagement, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_jardi), onNavigateToGarden)
    }
}

@Composable
fun PetitionsList (petitions: List<GardenMemberResponse>, onNavigateToPetitions: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(petitions.size) {
                arrayItem -> PetitionItem(petition = petitions[arrayItem], onNavigateToPetitions)
        }
    }
}

@Composable
fun PetitionItem(petition: GardenMemberResponse, onNavigateToPetitions: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!
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
                    text = petition.user.username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BraveGreen,
                    modifier = Modifier.padding(start = 8.dp).align(CenterVertically)
                )
            }
        }
        else {
            Column {
                Row(modifier = Modifier.align(Alignment.Start)) {
                    Image(
                        painter = painterResource(id = R.drawable.icona_peticions_jardi), "",
                        modifier = Modifier
                            .size(64.dp)
                            .align(CenterVertically)
                            .padding(start = 8.dp)
                    )
                    Text(
                        text = petition.user.username,
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
                                val garden = user.garden_info.garden.name
                                val petitioner = petition.user.username
                                CoroutineScope(Dispatchers.IO).launch {
                                    val petitionRequest = GardenInvitationRequest(garden = garden, user = petitioner)
                                    val call = getRetrofit()
                                        .create(APIService::class.java)
                                        .acceptGardenPetition(
                                            "Bearer " + tokenState.token,
                                            "gardens/$garden/requests/$petitioner",
                                            petitionRequest
                                        )

                                    if (call.isSuccessful) {
                                        call.body()?.let { tokenState.myPetitions(it) }
                                    } else {
                                        //ERROR MESSAGES, IF ANY
                                        error.value = call.code()
                                        openDialog.value = true
                                    }
                                }

                                onNavigateToPetitions()
                            })
                            .padding(8.dp)
                            .width(184.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = "Accept Petition!",
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

                                val garden = user.garden_info.garden.name
                                val petitioner = petition.user.username
                                CoroutineScope(Dispatchers.IO).launch {
                                    getRetrofit()
                                        .create(APIService::class.java)
                                        .refuseGardenPetition(
                                            "Bearer " + tokenState.token,
                                            "gardens/$garden/requests/$petitioner",
                                            garden = garden,
                                            user = petitioner
                                        )
                                }

                                onNavigateToPetitions()
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