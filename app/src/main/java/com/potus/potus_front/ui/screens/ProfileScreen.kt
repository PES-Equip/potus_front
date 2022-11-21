package com.potus.potus_front.ui.screens
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.ChangeUsernameRequest
import com.potus.potus_front.API.requests.DeleteAccountRequest
import com.potus.potus_front.API.requests.RegisterUserRequest
import com.potus.potus_front.MainActivity
import com.potus.potus_front.R
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
var Tokenprofile=true;
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun ProfileScreen(navController: NavController) {
    if(Tokenprofile) {


        val tokenState = TokenState.current
        val user = TokenState.current.user
        var username by remember { mutableStateOf(user?.username) }
        var email by remember { mutableStateOf(user?.email) }


        //poner el email del usuario pero no que no se pueda modificar mirar UserResponse
        //poner tambien los nombres de los potus que han muerto


        val notification = rememberSaveable { mutableStateOf("") }
        if (notification.value.isNotEmpty()) {
            Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
            notification.value = ""
        }

        var name by rememberSaveable { mutableStateOf("default name") }
        var bio by rememberSaveable { mutableStateOf("default bio") }

        var navigation_bool = false





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

                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = SoothingGreen)

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
                    colors = ButtonDefaults.buttonColors(backgroundColor = SoothingGreen)

                ) {
                    Text(text = "Save")
                }
            }
            Image(
                painter = painterResource(id = R.drawable.basic), "",
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Name", modifier = Modifier.width(100.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black
                    )
                )
            }

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Bio", modifier = Modifier
                        .width(100.dp)
                        .padding(top = 8.dp)
                )
                TextField(
                    value = bio,
                    onValueChange = { bio = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black
                    ),
                    singleLine = false,
                    modifier = Modifier.height(150.dp)
                )
            }
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {

                        val deleteAccountRequest = username?.let { DeleteAccountRequest(it) }
                        val call = getRetrofit().create(APIService::class.java)
                            .deleteAccount("Bearer " + tokenState.token, "user/profile")

                        if (call.isSuccessful) {
                            notification.value = "Account deleted"
                            tokenState.signUser(call.body())
                            navigation_bool = true
                        } else {
                            notification.value = "ERROR"
                            navigation_bool = true
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

        if (navigation_bool) {
            Tokenprofile=false;
            navigation_bool = false
            TokenFet=true;
            navController.navigate("auth_screen")
        }
    }
}
