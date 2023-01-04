package com.potus.potus_front.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.GardenRequest
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.*
import org.json.JSONObject


@Composable
fun CreateGardenScreen(onNavigateToProfile: () -> Unit, onNavigateToGarden: () -> Unit, onNavigateToInvitations: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToSelection: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    var actionString = ""
    val goToGarden = remember { mutableStateOf(false)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!.user

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = user.potus.waterLevel,
            collection = user.currency,
            username = user.username,
            addedWater = 0,
            addedLeaves = 0,
            onNavigateToProfile = { onNavigateToProfile() }
        )
        Column(modifier = Modifier.weight(1f).background(color = Daffodil)) {
            val newGardenName = remember { mutableStateOf(TextFieldValue()) }

            Spacer(modifier = Modifier.size(96.dp))
            Image(
                painter = painterResource(id = R.drawable.icona_nou_jardi), "",
                modifier = Modifier
                    .size(180.dp)
                    .align(CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(64.dp))
            OutlinedTextField(
                value = newGardenName.value,
                onValueChange = { newGardenName.value = it },
                label = { Text(text = "Your New Garden's Name") },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.icona_jardi),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = {
                    runBlocking {
                        val newGardenRequest = GardenRequest(name = newGardenName.value.text)

                        val call = getRetrofit()
                            .create(APIService::class.java)
                            .createGarden(
                                "Bearer " + tokenState.token,
                                "gardens",
                                newGardenRequest
                            )

                        val eBody = call.errorBody()
                        if (call.isSuccessful) {
                            call.body()?.let {
                                user.garden_info = it
                            }
                        } else {
                            //ERROR MESSAGES, IF ANY
                            openDialog.value = true
                            if (eBody != null) {
                                actionString = JSONObject(eBody.string()).getString("message")
                            } else {
                                actionString =  if (newGardenName.value.text.isEmpty()) "Namespace is empty"
                                                else if (newGardenName.value.text.length < 3) "Name must have at least 3 characters"
                                                else if (newGardenName.value.text.length > 30) "Name must have less than 30 characters"
                                                else "Name is invalid"
                            }
                        }
                    }
                    if (user.garden_info != null) onNavigateToGarden()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen),
                modifier = Modifier
                    .width(240.dp)
                    .height(80.dp)
                    .padding(16.dp)
                    .align(CenterHorizontally),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Create and Join!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SoothingGreen)
            }
        }
        if (openDialog.value) {
            Toast.makeText(LocalContext.current, actionString, Toast.LENGTH_LONG).show()
            openDialog.value = false
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_invitacions_jardins), onNavigateToInvitations, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_seleccio_jardi), onNavigateToSelection)
    }
}