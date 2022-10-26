package com.potus.potus_front.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.ActionRequest
import com.potus.potus_front.R
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

// En el composable Top Bar caldria descobrir com separar els composables de water i leaves.
@Composable
fun TopBar(waterLevel: Int, collection: Int, username: String) {
    Row(
        Modifier
            .background(color = BraveGreen)
            .fillMaxWidth()
            .height(64.dp)) {
        Box(modifier = Modifier
            .align(Alignment.CenterVertically)
            .width(64.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color(0x0CFFFFFF))){
            Text(modifier = Modifier
                .align(Alignment.Center),
                text = username )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier
            .align(Alignment.CenterVertically)
            .width(64.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color(0x0CFFFFFF))){
            Text(modifier = Modifier
                .align(Alignment.Center),
                text = "$waterLevel %" )
        }
        Spacer(modifier = Modifier.weight(0.02f))
        Box(modifier = Modifier
            .align(Alignment.CenterVertically)
            .width(64.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color(0x0CFFFFFF))){
            Text(modifier = Modifier
                .align(Alignment.Center),
                text = "$collection")
        }
        Spacer(modifier = Modifier.weight(0.075f))
    }
}

@Composable
fun CenterArea() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(128.dp))
        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally))
        {
            Image(
                painter = painterResource(id = R.drawable.test_basic),
                "",
                modifier = Modifier
                    .size(360.dp)
                    .align(Alignment.Center)
            )
            Image(
                painter = painterResource(id = R.drawable.planta_basic_tiges),
                "",
                modifier = Modifier
                    .size(360.dp)
                    .align(Alignment.Center)
            )
            Image(
                painter = painterResource(id = R.drawable.planta_basic_fulles),
                "",
                modifier = Modifier
                    .size(360.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun BottomBar(updateWaterLevel: (Int) -> Unit,
              updateLeaveRecollection:(Int) -> Unit) {
    val heightBottomBar = 192.dp
    val heightCircle = 125.dp
    val heightTotal = heightBottomBar+heightCircle/2
    val heightButton = 125.dp
    val widthButton = 140.dp
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(heightTotal)) {
        BoxWithConstraints(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(heightBottomBar)
                .background(BraveGreen)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightTotal)
            ) {
                Surface(
                    color = SoothingGreen,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(widthButton)
                        .height(heightButton)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    val tokenState = TokenState.current
                    val coroutineScope = rememberCoroutineScope()
                    Image(
                        painter = painterResource(id = R.drawable.icona_podar),
                        "",
                        modifier = Modifier
                            .clickable(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {

                                    val newUpdateActionRequest = ActionRequest("prune")
                                    getRetrofit().create(APIService::class.java)
                                        .actions("Bearer " + tokenState.token, "potus/actions", newUpdateActionRequest)
                                }

                                tokenState.user?.currency?.let { updateLeaveRecollection(it) }
                            })
                            .padding(8.dp)
                            .size(heightButton)
                            .align(Alignment.CenterVertically))
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = SoothingGreen,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(widthButton)
                        .height(heightButton)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    val tokenState = TokenState.current
                    val coroutineScope = rememberCoroutineScope()
                    Image(
                        painter = painterResource(id = R.drawable.icona_regadora),
                        "",
                        modifier = Modifier
                            .clickable(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {

                                    val newUpdateActionRequest = ActionRequest("watering")
                                    val call = getRetrofit().create(APIService::class.java)
                                        .actions("Bearer " + tokenState.token, "potus/actions", newUpdateActionRequest)

                                    if (call.isSuccessful) {
                                        val update = getRetrofit().create(APIService::class.java)
                                            .getUser("Bearer " + tokenState.token, "/user/profile")

                                        update.body()?.potus?.let { updateWaterLevel(it.waterLevel) }
                                    }

                                }

                                tokenState.user?.potus?.let {  }
                            })
                            .size(heightButton)
                            .align(Alignment.CenterVertically))
                }
            }
        }
        Surface(
            color = BraveGreen,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = (heightBottomBar - heightCircle / 2))
                .align(Alignment.TopCenter)
                .size(heightCircle)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icona_jardi),
                "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(heightCircle)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(color = SoothingGreen))
        }
    }
}