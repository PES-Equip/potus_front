package com.potus.potus_front.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.response.HistoryResponse
import com.potus.potus_front.composables.CenterArea
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.EarthBrown
import com.potus.potus_front.ui.theme.GraveStoneGray
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import java.util.*

@Composable
fun HistoryScreen(onNavigateToProfile: () -> Unit) {
    val openDialog = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf("") }
    val anyPotusDead = remember { mutableStateOf(true) }

    val user = TokenState.current.user!!.user

    val histEntries = remember {
        mutableStateOf(listOf(HistoryResponse(createdDate = Date(22/11/2022), deathDate = Date(24/11/2022), ""))) }

    val tokenState = TokenState.current
    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getHistory(
                "Bearer " + tokenState.token,
                "user/profile/history"
            )

        if (call.isSuccessful) {
            if (call.body()?.size!! > 0) {
                histEntries.value = call.body()!!
            }
            else { anyPotusDead.value = false }
        } else {
            //ERROR MESSAGES, IF ANY
            val jObjErr = call.errorBody()?.string()?.let { JSONObject(it) }
            if (jObjErr != null) {
                error.value = jObjErr.getString("message")
            }
            openDialog.value = true
        }
    }

    Column(Modifier.background(color = EarthBrown)) {
        TopBar(
            waterLevel = user.potus.waterLevel, //waterLevelState,
            collection = user.currency, //collection,
            username = "Mr. Simon", //user.username,
            addedWater = 0, //addedWater,
            addedLeaves = 0, //addedLeaves
            onNavigateToProfile = { onNavigateToProfile() }
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Historial(entries = histEntries.value, anyPotusDead.value)
        Spacer(modifier = Modifier.weight(0.1f))
    }

    if (openDialog.value) {
        Toast.makeText(LocalContext.current, error.value, Toast.LENGTH_SHORT).show()
        openDialog. value = false
    }
}

@Composable
fun Historial(entries: List<HistoryResponse>, print: Boolean) {
    if (print) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 16.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            //var entries = arrayListOf<String>("Mr.Simon", "Mr.Simon the II", "Mr.Simon the III")
            items(entries.size) { arrayItem ->
                RowItem(item = entries[arrayItem])
            }
        }
    }
    else {
        Column {
            Spacer(modifier = Modifier.weight(0.1f))
            Surface(
                color = GraveStoneGray,
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp)
                ) {
                    Text(
                        text = "No Dead Potus Found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

@Composable
fun RowItem(item: HistoryResponse) {
    Surface(
        color = GraveStoneGray,
        modifier = Modifier.clip(RoundedCornerShape(16.dp)).padding(horizontal = 16.dp)) {
        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .width(360.dp)
        ) {
            items(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                ) {
                    CenterArea(plantState = "DEFAULT", true)
                }
                Text(
                    text = item.name,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Adoption Date: " + item.createdDate + "\n\n"
                            + "Death Date: " + item.deathDate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 64.dp, start = 32.dp, end = 32.dp),
                    fontSize = 25.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
