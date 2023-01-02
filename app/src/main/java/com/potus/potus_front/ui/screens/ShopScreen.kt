package com.potus.potus_front.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.BuyBonusRequest
import com.potus.potus_front.API.requests.DeleteAccountRequest
import com.potus.potus_front.API.requests.InformLocationRequest
import com.potus.potus_front.R
import com.potus.potus_front.composables.BottomBar
import com.potus.potus_front.composables.CenterArea
import com.potus.potus_front.composables.GasesWindow
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ShopScreen(onNavigateToHome: () -> Unit, onNavigateToProfile: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val user = TokenState.current.user!!.user
    var username by remember { mutableStateOf(user?.username) }
    var waterLevelState by remember { mutableStateOf(user.potus.waterLevel) }
    var collection by remember { mutableStateOf(user.currency) }
    var addedWater by remember { mutableStateOf(0) }
    var addedLeaves by remember { mutableStateOf(0) }
    var plantState by remember { mutableStateOf("DEFAULT") }
    //var thematicEvent by remember { mutableStateOf("DEFAULT") }
    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

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
            tokenState.user?.user?.potus?.let { plantState = it.state }
        } else {
            //ERROR MESSAGES, IF ANY
            error.value = call.code()
            openDialog.value = true
        }
    }

    Column(Modifier.background(color = SoothingGreen),verticalArrangement = Arrangement.spacedBy(1.dp)) {
        TopBar(
            waterLevel = waterLevelState,
            collection = collection,
            username = user.username,
            addedWater = addedWater,
            addedLeaves = addedLeaves,
            onNavigateToProfile = {onNavigateToProfile()},
            onNavigateToShop = { }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Image(
                painter = painterResource(id = R.drawable.newtext), "",
                modifier = Modifier
                    .size(400.dp)
                    )



        }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.fertilizer_bonus), "",
                    modifier = Modifier
                        .size(150.dp)

                )

            }
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = "Price: 500")
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.icona_currency),
                contentDescription = "")

        }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {


                        CoroutineScope(Dispatchers.Main).launch {


                            val buyBonusRequest = username?.let { BuyBonusRequest(it) }
                            val call = buyBonusRequest?.let {
                                getRetrofit().create(APIService::class.java)
                                    .buyBonus(
                                        "Bearer " + tokenState.token,
                                        "potus/store/buy/" +
                                        it.modifier
                                    )
                            }

                            if (call.isSuccessful) {
                                notification.value = "Fertilizer bought!"
                                tokenState.signUser(call.body())
                            } else {
                                notification.value = "ERROR"
                            }
                        }



                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen)

                ) {
                    Text(text = "Buy fertilizer")
                }
                Button(
                    onClick = {
                        onNavigateToHome()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen)

                ) {
                    Text(text = "Go back to your Potus")
                }
            }


    }

}

