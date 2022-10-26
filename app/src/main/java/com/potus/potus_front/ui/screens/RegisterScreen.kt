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
import timber.log.Timber


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun RegisterScreen() {
    val coroutineScope = rememberCoroutineScope()
    val tokenState = TokenState.current

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
                    Timber.d("BAD")
                }
            }

        }) {
        }
    }
}