package com.potus.potus_front.ui.screens
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.ChangeUsernameRequest
import com.potus.potus_front.API.requests.DeleteAccountRequest
import com.potus.potus_front.composables.CenterArea
import com.potus.potus_front.composables.GasesWindow
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToTrophies: () -> Unit, 
    onNavigateToFavourite: () -> Unit)
{
    val tokenState = TokenState.current
    val user = TokenState.current.user?.user
    var username by remember { mutableStateOf(user?.username) }
    var email by remember { mutableStateOf(user?.email) }

    var plantState by remember { mutableStateOf("DEFAULT") }

    //poner el email del usuario pero no que no se pueda modificar mirar UserResponse
    //poner tambien los nombres de los potus que han muerto

    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    var name by rememberSaveable { mutableStateOf("default name") }
    var bio by rememberSaveable { mutableStateOf("default bio") }

    Column(
        modifier = Modifier
            .background(color = SoothingGreen)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    notification.value = "Cancelled"
                    onNavigateToHome()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen)

            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {

                        val changeUsernameRequest = username?.let { ChangeUsernameRequest(it) }
                        val call = changeUsernameRequest?.let {
                            getRetrofit().create(APIService::class.java)
                                .changeUsername(
                                    "Bearer " + tokenState.token,
                                    "user/profile",
                                    it
                                )
                        }

                        if (call?.isSuccessful == true) {
                            notification.value = "Profile updated"
                            if (call != null) {
                                tokenState.signUser(call.body())
                            }
                        } else {
                            notification.value = "ERROR"
                        }
                    }

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen)

            ) {
                Text(text = "Save")
            }
        }

        CenterArea(plantState = plantState, textDisplay = true)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp))
            username?.let {
                TextField(
                    value = it,
                    onValueChange = { username = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black
                    )
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Email", modifier = Modifier.width(100.dp))
            email?.let {
                TextField(
                    value = it,
                    onValueChange = { email = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black
                    )
                )
            }
        }

        Button(onClick = onNavigateToTrophies,
            colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "See trophies")
        }

        Button(onClick = onNavigateToHistory,
            colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "See Potus Memorial")
        }
        Button(onClick = onNavigateToFavourite,
            colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "See Favourite Meetings")
        }

        Surface(color = SoothingGreen, modifier = Modifier.weight(1f)) {
            //CenterArea(thematicEvent, plantState)
            CenterArea(plantState, true)
        }

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {

                    val deleteAccountRequest = username?.let { DeleteAccountRequest(it) }
                    val call = getRetrofit().create(APIService::class.java)
                        .deleteAccount("Bearer " + tokenState.token, "user/profile")

                    if (call.isSuccessful) {
                        notification.value = "Account deleted"
                        tokenState.signUser(call.body())
                        onNavigateToAuth()
                    } else {
                        notification.value = "ERROR"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium

        ) {
            Text(text = "Delete Account")
        }
    }
}
