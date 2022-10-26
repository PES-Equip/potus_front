package com.potus.potus_front.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.RegisterUserRequest
import com.potus.potus_front.models.TokenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.json.JSONObject
//import timber.log.Timber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.unit.dp
import com.potus.potus_front.R
import com.potus.potus_front.controllers.PotusController
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Potus_frontTheme
import com.potus.potus_front.ui.theme.Shapes
import com.potus.potus_front.ui.theme.SoothingGreen


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Preview
@Composable
fun RegisterScreen() {
    val coroutineScope = rememberCoroutineScope()
    val tokenState = TokenState.current
    val openDialog = remember { mutableStateOf(false)  }

    Surface(
        color = MaterialTheme.colors.primaryVariant,
        contentColor = MaterialTheme.colors.onSurface,
    ) {
        val (username, onUserNameChange) = remember {
            mutableStateOf("")
        }
        val (password, onPasswordChange) = remember {
            mutableStateOf("")
        }
        val (checked, onCheckedChange) = remember {
            mutableStateOf(false)
        }
        val textState = remember { mutableStateOf(TextFieldValue()) }
        Column {
            Spacer(modifier = Modifier.size(32.dp))


            Image(painter = painterResource(id = R.drawable.logintext), "",
                modifier = Modifier
                    .size(200.dp)
                    .height(150.dp)
                    .align(Alignment.CenterHorizontally))
            Image(painter = painterResource(id = R.drawable.basic), "",
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                label = { Text(text = "Username") },
                leadingIcon =  {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null
                )},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.size(16.dp))
            Button(

                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {

                        val registerUserRequest = RegisterUserRequest(textState.value.text)
                        val call = getRetrofit().create(APIService::class.java)
                            .registerUser("Bearer " + tokenState.token, "user", registerUserRequest)

                        if(textState.value.text == "maxvives"){
                            openDialog.value =true
                            //call.isSuccessful = false
                        }

                        if (call.isSuccessful) {
                            tokenState.signUser(call.body())
                        } else {
                            //Timber.d("BAD")
                        }
                    }



                },
                colors = ButtonDefaults.buttonColors(backgroundColor = SoothingGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Log In")
            }
            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Dialog Title")
                    },
                    text = {
                        Text("Here is a text ")
                    },
                    confirmButton = {
                        Button(

                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("This is the Confirm Button")
                        }
                    },
                    dismissButton = {
                        Button(

                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("This is the dismiss Button")
                        }
                    }
                )
            }
        }


    }
}
/*
    Column(Modifier.padding(16.dp)) {
        val textState = remember { mutableStateOf(TextFieldValue()) }


        TextField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {

                val registerUserRequest = RegisterUserRequest(textState.value.text)
                val call = getRetrofit().create(APIService::class.java)
                    .registerUser("Bearer " + tokenState.token, "user", registerUserRequest)

                if (call.isSuccessful) {
                    tokenState.signUser(call.body())
                } else {
                    //Timber.d("BAD")
                }
            }

        }) {
        }
    }

}
*/
