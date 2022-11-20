package com.potus.potus_front.ui.screens

import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.ActionRequest
import com.potus.potus_front.API.requests.InformLocationRequest
import com.potus.potus_front.composables.BottomBar
import com.potus.potus_front.composables.CenterArea
import com.potus.potus_front.composables.GasesWindow
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun HistoryScreen() {
    /*val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val user = TokenState.current.user!!
    var waterLevelState by remember { mutableStateOf(user.potus.waterLevel) }
    var collection by remember { mutableStateOf(user.currency) }
    var addedWater by remember { mutableStateOf(0) }
    var addedLeaves by remember { mutableStateOf(0) }
    var plantState by remember { mutableStateOf("DEFAULT") }
    //var thematicEvent by remember { mutableStateOf("DEFAULT") }

    val tokenState = TokenState.current
    LaunchedEffect(Dispatchers.IO) {
        val newUpdateStateRequest = InformLocationRequest(latitude = tokenState.location.first, length = tokenState.location.second)
        val call = getRetrofit()
            .create(APIService::class.java)
            .informLocation(
                "Bearer " + tokenState.token,
                "potus/events",
                newUpdateStateRequest
            )

        if (call.isSuccessful) {
            tokenState.myPotus(call.body())
            tokenState.user?.potus?.let { plantState = it.state }
        } else {
            //ERROR MESSAGES, IF ANY
            error.value = call.code()
            openDialog.value = true
        }
    }
*/
    Column(Modifier.background(color = EarthBrown)) {
        TopBar(
            waterLevel = 100, //waterLevelState,
            collection = 1400, //collection,
            username = "Mr. Simon", //user.username,
            addedWater = 0, //addedWater,
            addedLeaves = 0, //addedLeaves
        )

        Historial()

    }



    /*if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                openDialog.value = false
            },
            text = {
                Text(
                    text = "ERROR " + error.value + "! Could not establish a working connection")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("Ok")
                }
            }
        )
    }*/
}

@Composable
fun Historial() {
    LazyRow(modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        var entries = arrayListOf<String>("Mr.Simon", "Mr.Simon the II", "Mr.Simon the III")
        items(entries.size) {
                arrayItem->
            RowItem(item = entries[arrayItem])
        }
    }
}

@Composable
fun RowItem(item: String) {
    Surface(
        color = GraveStoneGray,
        modifier = Modifier.clip(RoundedCornerShape(16.dp))) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .width(360.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)) {
                CenterArea(plantState = "DEFAULT")
            }
            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 64.dp),
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }
    }
}
