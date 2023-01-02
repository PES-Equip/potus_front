package com.potus.potus_front.ui.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.response.data_models.SimplifiedGardenMember
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random


@Composable
fun GardenScreen(onNavigateToProfile: () -> Unit, onNavigateToManagement: () -> Unit, onNavigateToShop: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToChat: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!.user
    val members = remember { mutableStateOf(setOf(SimplifiedGardenMember("THERE ARE NO USERS IN THIS GARDEN", "OWNER"))) }

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getGardenMembers(
                "Bearer " + tokenState.token,
                "gardens/profile/members"
            )

        if (call.isSuccessful) {
            call.body()?.let { members.value = it }
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
            Spacer(modifier = Modifier.size(8.dp))
            Surface(modifier = Modifier.fillMaxWidth().clickable(onClick = { onNavigateToManagement() }), color = Color.Transparent) {
                Text(
                    //text = "MY GARDEN",
                    text = tokenState.user?.user?.garden_info?.garden?.name.toString(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = BraveGreen,
                    textAlign = TextAlign.Center
                )
            }
            //MembersList(listOf(Pair("Me", "OWNER"), Pair("You", "ADMIN"), Pair("He", "ADMIN"), Pair("She", "ADMIN"), Pair("They", "ADMIN")))
            MembersList(members.value)
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_mercat), onNavigateToShop, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_xat), onNavigateToChat)
    }
}

@Composable
fun MembersList (members: Set<SimplifiedGardenMember>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(members.size) {
                arrayItem -> MemberItem(member = members.elementAt(arrayItem))
        }
    }
}

@Composable
fun MemberItem(member: SimplifiedGardenMember) {
    val gases = arrayOf("C6H6", "Cl2", "CO", "H2S", "HCl", "HCNM", "HCT", "Hg", "NO2", "NO", "NOX", "O3", "PM1", "PM2_5", "PM10", "PS", "SO2")
    var toggled by remember { mutableStateOf(false) }
    val randomAvatar = gases[Random.nextInt(gases.size)]

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
                    text = member.username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BraveGreen,
                    modifier = Modifier.padding(start = 8.dp).align(CenterVertically)
                )
            }
            else {
                Surface (modifier = Modifier.fillMaxWidth(), color = Color.Transparent) {
                    Text(
                        text = "\n" + member.username + "\n\nRole: " + member.role + "\n",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp).align(CenterVertically)
                    )
                }
            }
        }
    }
}