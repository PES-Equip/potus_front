package com.potus.potus_front.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.ActionRequest
import com.potus.potus_front.API.requests.InformLocationRequest
import com.potus.potus_front.composables.BottomBar
import com.potus.potus_front.composables.CenterArea
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen() {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val user = TokenState.current.user!!
    var waterLevelState by remember { mutableStateOf(user.potus.waterLevel) }
    var collection by remember { mutableStateOf(user.currency) }
    var plantState by remember { mutableStateOf("DEFAULT") }
    var thematicEvent by remember { mutableStateOf("DEFAULT") }

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

    Column(Modifier.background(color = SoothingGreen)){
        TopBar(waterLevel = waterLevelState, collection = collection, username = user.username)
        Surface(color = SoothingGreen, modifier = Modifier.weight(1f)) {
            CenterArea(thematicEvent, plantState)
        }
        BottomBar(
            updateWaterLevel = {
                    newWaterLevel ->
                waterLevelState = newWaterLevel
            },
            updateLeaveRecollection = {
                    collectedLeaves ->
                collection = collectedLeaves
            }
        )
    }

    if (openDialog.value) {

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
    }
}