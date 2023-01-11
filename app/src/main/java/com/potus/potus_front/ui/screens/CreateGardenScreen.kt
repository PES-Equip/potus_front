package com.potus.potus_front.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun CreateGardenScreen(onNavigateToProfile: () -> Unit, onNavigateToGarden: () -> Unit, onNavigateToInvitations: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToSelection: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val actionString = remember { mutableStateOf("")  }
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
        Column(modifier = Modifier
            .weight(1f)
            .background(color = Daffodil)) {
            val newGardenName = remember { mutableStateOf(TextFieldValue()) }

            Spacer(modifier = Modifier.weight(0.2f))
            Box(modifier = Modifier
                .weight(0.45f)
                .align(CenterHorizontally)){
                Image(
                    painter = painterResource(id = R.drawable.icona_nou_jardi), "",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.weight(0.15f))
            OutlinedTextField(
                value = newGardenName.value,
                onValueChange = { newGardenName.value = it },
                label = { Text(text = "Your New Garden's Name") },
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 8.dp),
                        painter = painterResource(id = R.drawable.icona_jardi),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .weight(0.175f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.weight(0.075f))
            Button(
                onClick = {
                    GlobalScope.launch(Dispatchers.IO) {
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
                            tokenState.myGarden(call.body())
                            goToGarden.value = true
                        } else {
                            //ERROR MESSAGES, IF ANY
                            openDialog.value = true
                            if (eBody != null) {
                                actionString.value = JSONObject(eBody.string()).getString("message")
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen),
                modifier = Modifier
                    .width(240.dp)
                    .weight(0.25f)
                    .padding(16.dp)
                    .align(CenterHorizontally),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Create and Join!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SoothingGreen)
            }
            if (openDialog.value) {
                Toast.makeText(LocalContext.current, actionString.value, Toast.LENGTH_LONG).show()
                if (goToGarden.value) {
                    onNavigateToGarden()
                    goToGarden.value = false
                }
                openDialog.value = false
            }
        }
        Spacer(modifier = Modifier.weight(0.025f))
        GardenBottomBar(painterResource(id = R.drawable.icona_invitacions_jardins), onNavigateToInvitations, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_seleccio_jardi), onNavigateToSelection)
    }
}