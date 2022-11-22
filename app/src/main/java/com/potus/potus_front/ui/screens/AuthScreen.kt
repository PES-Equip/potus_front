package com.potus.potus_front.ui.screens

import android.content.IntentSender.SendIntentException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.R
import com.potus.potus_front.google.*
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.component.SignInButtons
import com.potus.potus_front.ui.theme.BraveGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber



@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable

fun AuthScreen(navController : NavHostController) { //onNavigateToSwitcher: () -> Unit) {
    var text by remember { mutableStateOf<String?>(null) }
    val tokenState = TokenState.current
    val signInRequestCode = 1

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            try {
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    text = "Google sign in failed"
                } else {
                    if(account.idToken != null) {
                        tokenState.signToken(account.idToken!!)
                        //onNavigateToSwitcher
                        navController.navigate(Screen.SwitcherScreen.route)
                    }
                    else {
                        text = "Google sign in failed"
                    }
                }
            } catch (e: ApiException) {
                text = "Google sign in failed"
            }
        }

        AuthView(
            errorText = text,
            onClick = {
                text = null
                authResultLauncher.launch(signInRequestCode)
            }
        )
}


@ExperimentalMaterialApi
@Composable
fun AuthView(
    errorText: String?,
    onClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    Scaffold {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.titol), "",
                modifier = Modifier.height(150.dp)
                    .size(360.dp)
                    .align(Alignment.CenterHorizontally))
            Image(painter = painterResource(id = R.drawable.basic), "",
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.CenterHorizontally))

            SignInButtons(
                text = "Sign in with Google",
                backgroundColor = BraveGreen,
                loadingText = "Signing in...",
                isLoading = isLoading,
                icon = painterResource(id = R.drawable.ic_google_logo),
                onClick = {
                    isLoading = true
                    onClick()
                }
            )

            errorText?.let {
                isLoading = false
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = it)
            }
        }
    }
}