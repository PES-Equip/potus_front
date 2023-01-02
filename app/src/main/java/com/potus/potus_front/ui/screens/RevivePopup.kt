package com.potus.potus_front.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.PotusReviveRequest
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun RevivePopup(onNavigateToHome: () -> Unit) {
    Surface(modifier = Modifier
        .fillMaxSize(),
        color = BraveGreen
    ) {
        val tokenState = TokenState.current
        val textState = remember { mutableStateOf(TextFieldValue()) }
        val displayErrorText = remember { mutableStateOf(false) }
        val errorText = remember { mutableStateOf("") }

        Column {
            Spacer(modifier = Modifier.weight(0.075f))
            
            Surface(modifier = Modifier
                .weight(0.85f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(46.dp)),
                color = Color.White
            )
            {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()) {

                    Spacer(modifier = Modifier.weight(0.15f))

                    Text(
                        modifier = Modifier
                            .weight(0.3f)
                            .align(Alignment.CenterHorizontally),
                        text = "Your Potus\nDied",
                        fontSize = 50.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.weight(0.15f))

                    Text(
                        modifier = Modifier
                            .weight(0.45f)
                            .align(Alignment.CenterHorizontally),
                        text = "Your Potus has died! \n\n" +
                                "Your buddy is now part of your past Potus collection. \n\n" +
                                "Now, pick a name for your next Potus!",
                        fontSize = 20.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.weight(0.1f))

                    OutlinedTextField(
                        modifier = Modifier
                            .weight(0.15f)
                            .fillMaxWidth(),
                        value = textState.value,
                        onValueChange = { textState.value = it },
                        label = { Text(text = "New name") },
                        maxLines = 1,
                        shape = MaterialTheme.shapes.medium
                    )

                    Spacer(modifier = Modifier.weight(0.15f))

                    Button(
                        modifier = Modifier
                            .weight(0.15f)
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {

                                val newRevivePotusRequest =
                                    PotusReviveRequest(textState.value.toString())
                                val call = getRetrofit()
                                    .create(APIService::class.java)
                                    .revivePotus(
                                        "Bearer " + tokenState.token,
                                        "user/profile/potus",
                                        newRevivePotusRequest
                                    )
                                val body = call.body()
                                val ebody = call.errorBody()

                                if (call.isSuccessful && body != null) {
                                    tokenState.user?.let {
                                        tokenState.user!!.user.potus = body
                                        onNavigateToHome
                                    }
                                } else {
                                    if (ebody != null) {
                                        val jObjErr = JSONObject(ebody.string())
                                        errorText.value = jObjErr.getString("message")
                                        displayErrorText.value = true
                                    }
                                }
                            }
                            // navigate
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = SoothingGreen)
                    ) {
                        Text(text = "Revive your Son")
                    }

                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
            
            Spacer(modifier = Modifier.weight(0.075f))
        }
        if(displayErrorText.value) {
            Toast.makeText(LocalContext.current, errorText.value, Toast.LENGTH_SHORT).show()
            displayErrorText.value = false
        }
    }
}