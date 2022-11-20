package com.potus.potus_front.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.GardenRequest
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
fun CreateGardenScreen() {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!
    var newGarden = remember { mutableStateOf(Triple("NEW GARDEN", 0, "")) }

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = user.potus.waterLevel,
            collection = user.currency,
            username = user.username,
            addedWater = 0,
            addedLeaves = 0
        )
        Column(modifier = Modifier.weight(1f).background(color = Daffodil)) {
            var newGardenName = remember { mutableStateOf(TextFieldValue()) }

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
                    CoroutineScope(Dispatchers.IO).launch {
                        val newGardenRequest = GardenRequest(name = newGardenName.value.text)
                            val call = getRetrofit()
                                .create(APIService::class.java)
                                .createGarden(
                                    "Bearer " + tokenState.token,
                                    "gardens",
                                    newGardenRequest
                                )

                            if (call.isSuccessful) {
                                call.body()?.let { newGarden.value = it.garden }
                            } else {
                                //ERROR MESSAGES, IF ANY (OpenDialog not present because error messages have been changed)
                                error.value = call.code()
                                openDialog.value = true
                            }
                    }
                    /* SWITCHER: a la vista del nou Jardi */
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
        GardenBottomBar(painterResource(id = R.drawable.icona_invitacions_jardins), painterResource(id = R.drawable.basic), painterResource(id = R.drawable.icona_seleccio_jardi))
    }
}