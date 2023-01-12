package com.potus.potus_front.ui.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.response.GasesResponse
import com.potus.potus_front.API.response.NewGardenResponse
import com.potus.potus_front.API.response.data_models.GasRegistry
import com.potus.potus_front.API.response.data_models.Meeting
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingsScreen(onNavigateToProfile: () -> Unit, onNavigateToChat: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToGarden: () -> Unit, onNavigateToShop: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!.user

    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    LaunchedEffect(Dispatchers.IO) {

        val call = getRetrofit()
            .create(APIService::class.java)
            .getMeetingsList(
                "Bearer " + tokenState.token,
                "meetings",
                "01-02-2023",
                41.4,
                2.16,
                "01-01-2023"
            )

        if (call.isSuccessful) {
            //Timber.tag("HERE!").d(call.body()?.toString())
            call.body()?.let { tokenState.allMeetings(it) }
        } else {
            //ERROR MESSAGES, IF ANY
            error.value = call.code()
            openDialog.value = true
            val Ebody = call.errorBody()
            if (Ebody != null) {
                var jObjErr = JSONObject(Ebody.string())
                notification.value = jObjErr.getString("message")
                print(jObjErr.getString("message"))
            }
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
            //GardenList(listOf(NewGardenResponse("We Are The Champions", 3, "You should ask to join us."), NewGardenResponse("#1 GARDEN IN THE WORLD", 0, "Admire us!"), NewGardenResponse("WOOOHOOOOOOO", 0, "HEEEEY"), NewGardenResponse("Christmas gang :)", 156, "Fum, fum, fum"), NewGardenResponse("Developer's corner", 1, "So tired..."), NewGardenResponse("Knights of the PIC Table", 0, "Hehe."), NewGardenResponse("NO-NAME", 0, "Join us! We do bite ;)"), NewGardenResponse("Bosc", 6, "Els originals!")))

            //MeetingsList(listOf(Meeting("Carrer PotusLand","Barcelona", Date(),1, GasesResponse("codigas",1.0,2.0,"nom",registry = mapOf(Pair("", GasRegistry("", "NO DATA", "mg_m3", 0.0)))),Date(),"Veniu tots!","Quedada de potutus")))
            MeetingsList(tokenState.meetings)
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_jardi), onNavigateToGarden, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_xat), onNavigateToChat)
    }
}

@Composable
fun MeetingsList (meetings: List<Meeting>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(meetings.size) {
                arrayItem -> MeetingsItem(meeting = meetings[arrayItem])
        }
    }
}

@Composable
fun MeetingsItem(meeting: Meeting) {
    val tokenState = TokenState.current
    var toggled by remember { mutableStateOf(false) }

    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }


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
        if (!toggled) Text(text = meeting.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BraveGreen)
        else {
            Column() {
                Row(modifier = Modifier.align(Alignment.Start)) {
                    Text(
                        text = "\n" + meeting.title + "\n\nCiutat: " + meeting.city + "\nData inici: " + meeting.start_date + "\n" + "\nAdreça: " + meeting.address + "\n",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp).align(CenterVertically)
                    )
                }
                Surface(
                    color = BraveGreen,
                    modifier = Modifier
                        .clickable(onClick = {
                            val askedGardenName = meeting.id

                            CoroutineScope(Dispatchers.IO).launch {
                                getRetrofit()
                                    .create(APIService::class.java)
                                    .addFavouriteMeetingsList(
                                        "Bearer " + tokenState.token,
                                        "user/meeting/$askedGardenName",
                                        meetingId = askedGardenName
                                    )
                            }

                            notification.value = "Meeting added to favourite!"
                            /* POP-UP? */
                        })
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "Add to favourites",
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