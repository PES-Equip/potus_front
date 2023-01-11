package com.potus.potus_front.ui.screens

import android.graphics.Paint.Align
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import com.potus.potus_front.composables.*
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

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

        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.bubble_text), "",
            modifier = Modifier.fillMaxWidth()
        )
            Image(
                painter = painterResource(id = R.drawable.botiga_venedora), "",
                modifier = Modifier.aspectRatio(16f / 9f)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f)

            ) {
                Column()
                {
                    Image(
                        painter = painterResource(id = R.drawable.botiga_fertilitzant), "",
                        modifier = Modifier
                            .weight(1f)
                    )
                    Row(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(0.25f))
                    {
                        Text(text = "Price: 240")
                        Image(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(id = R.drawable.icona_currency),
                            contentDescription = "")
                    }
                    Button(
                        modifier = Modifier
                            .weight(0.25f),
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                val buyBonusRequest = username?.let { BuyBonusRequest(it) }
                                val call = buyBonusRequest?.let {
                                    getRetrofit().create(APIService::class.java)
                                        .buyBonus(
                                            "Bearer " + tokenState.token,
                                            "potus/store/buy/" +
                                                    "PRUNE_MAX_CURRENCY_PERMANENT_BUFF",
                                            modifier = "PRUNE_MAX_CURRENCY_PERMANENT_BUFF"
                                        )
                                }

                                if (call.isSuccessful) {
                                    val currencycall = getRetrofit().create(APIService::class.java)
                                        .getUser("Bearer " + tokenState.token, "user")
                                    tokenState.signUser(currencycall.body())
                                    collection = tokenState.user?.user?.currency!!
                                    notification.value = "Fertilizer bought!"

                                } else {
                                    val Ebody = call.errorBody()
                                    if (Ebody != null) {
                                        var jObjErr = JSONObject(Ebody.string())
                                        notification.value = jObjErr.getString("message")
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen)
                    ) {
                        Text(text = "Buy fertilizer")
                    }
                    Spacer(modifier = Modifier.weight(0.05f))
                }
                Column()
                {
                    Image(
                        painter = painterResource(id = R.drawable.botiga_fertilitzant_blau), "",
                        modifier = Modifier
                            .weight(1f)
                    )
                    Row(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(0.25f))
                    {
                        Text(text = "Price: 120")
                        Image(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(id = R.drawable.icona_currency),
                            contentDescription = "")
                    }
                    Button(
                        modifier = Modifier
                            .weight(0.25f),
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                val buyBonusRequest = username?.let { BuyBonusRequest(it) }
                                val call = buyBonusRequest?.let {
                                    getRetrofit().create(APIService::class.java)
                                        .buyBonus(
                                            "Bearer " + tokenState.token,
                                            "potus/store/buy/" +
                                                    "PRUNE_CURRENCY_PERMANENT_BUFF",
                                            modifier = "PRUNE_CURRENCY_PERMANENT_BUFF"
                                        )
                                }
                                if (call.isSuccessful) {
                                    val currencycall = getRetrofit().create(APIService::class.java)
                                        .getUser("Bearer " + tokenState.token, "user")
                                    tokenState.signUser(currencycall.body())
                                    collection = tokenState.user?.user?.currency!!
                                    notification.value = "Fertilizer bought!"
                                } else {
                                    val Ebody = call.errorBody()
                                    if (Ebody != null) {
                                        var jObjErr = JSONObject(Ebody.string())
                                        notification.value = jObjErr.getString("message")
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen)
                    ) {
                        Text(text = "Buy fertilizer")
                    }
                    Spacer(modifier = Modifier.weight(0.05f))
                }
                Column()
                {
                    Image(
                        painter = painterResource(id = R.drawable.botiga_fertilitzant_vermell), "",
                        modifier = Modifier
                            .weight(1f)
                    )
                    Row(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(0.25f))
                    {
                        Text(text = "Price: 120")
                        Image(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(id = R.drawable.icona_currency),
                            contentDescription = "")
                    }
                    Button(
                        modifier = Modifier
                            .weight(0.25f),
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                val buyBonusRequest = username?.let { BuyBonusRequest(it) }
                                val call = buyBonusRequest?.let {
                                    getRetrofit().create(APIService::class.java)
                                        .buyBonus(
                                            "Bearer " + tokenState.token,
                                            "potus/store/buy/" +
                                                    "WATER_PERMANENT_INCREASE",
                                            modifier = "WATER_PERMANENT_INCREASE"
                                        )
                                }
                                if (call.isSuccessful) {
                                    notification.value = "Fertilizer bought!"
                                } else {
                                    val Ebody = call.errorBody()
                                    if (Ebody != null) {
                                        var jObjErr = JSONObject(Ebody.string())
                                        notification.value = jObjErr.getString("message")
                                    }
                                }
                            }

                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen)
                    ) {
                        Text(text = "Buy fertilizer")
                    }
                    Spacer(modifier = Modifier.weight(0.05f))
                }
            }

        GardenBottomBar(painterResource(id = R.drawable.basic), onNavigateToHome)
    }

}

