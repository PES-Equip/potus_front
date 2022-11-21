package com.potus.potus_front.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.potus.potus_front.API.response.GardenListResponse
import com.potus.potus_front.API.response.NewGardenResponse
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Preview
@Composable
fun SelectGardenScreen() {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getGardenList(
                "Bearer " + tokenState.token,
                "gardens"
            )

        if (call.isSuccessful) {
            call.body()?.let { tokenState.allGardens(it) }
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
            GardenList(tokenState.gardens)
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_invitacions_jardins), painterResource(id = R.drawable.basic), painterResource(id = R.drawable.icona_nou_jardi))
    }
}

@Composable
fun GardenList (gardens: List<NewGardenResponse>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gardens.size) {
            arrayItem -> GardenItem(garden = gardens[arrayItem])
        }
    }
}

@Composable
fun GardenItem(garden: NewGardenResponse) {
    val tokenState = TokenState.current
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!toggled) Text(text = garden.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BraveGreen)
        else {
            Column() {
                Row(modifier = Modifier.align(Alignment.Start)) {
                    Text(
                        text = "\n" + garden.name + "\n\nMembers: " + garden.members_num.toString() + "\nAbout: " + garden.description + "\n",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp).align(CenterVertically)
                    )
                }
                Surface(
                    color = BraveGreen,
                    modifier = Modifier
                        .clickable(onClick = {
                                val askedGardenName = garden.name
                                CoroutineScope(Dispatchers.IO).launch {
                                    val gardenRequest = GardenRequest(name = askedGardenName)
                                    val call = getRetrofit()
                                        .create(APIService::class.java)
                                        .askToJoinGarden(
                                            "Bearer " + tokenState.token,
                                            "gardens/profile/requests/$askedGardenName",
                                            gardenRequest
                                        )
                                }

                                /* POP-UP? */
                            })
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "Send Join Request!",
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