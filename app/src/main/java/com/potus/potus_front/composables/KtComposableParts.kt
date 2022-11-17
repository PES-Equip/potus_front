package com.potus.potus_front.composables

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.painter.Painter
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.util.Collections.list

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopBar(waterLevel: Int, collection: Int, username: String, addedWater: Int, addedLeaves: Int) {
    Row(
        Modifier
            .background(color = BraveGreen)
            .fillMaxWidth()
            .height(64.dp)) {
        Box(modifier = Modifier
            .padding(start = 16.dp)
            .align(Alignment.CenterVertically)
            .width((username.length * 10).dp)
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
            .width(80.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color(0x0CFFFFFF))){

            Image(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 4.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.icona_droplet),
                contentDescription = "")

            Text(modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
                text = "$waterLevel %")

        }
        Spacer(modifier = Modifier.weight(0.02f))
        Box(modifier = Modifier
            .padding(end = 16.dp)
            .align(Alignment.CenterVertically)
            .width(80.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color(0x0CFFFFFF))){
            Image(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 4.dp)
                    .size(24.dp),
                painter = painterResource(id = R.drawable.icona_currency),
                contentDescription = "")
            Text(modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
                text = "$collection")
        }
    }
}

@Composable
fun CenterArea(thematicEvent:String, plantState:String) {
    val plant = PlantEvents(plantState)
    val tiges = plant[0]
    var fulles = painterResource(id = R.drawable.planta_basic_fulles)
    if (plant.size == 2) fulles = plant[1]
    val test = ThematicEvents(thematicEvent)
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(128.dp))
        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally))
        {
            Image(
                painter = test,
                "",
                modifier = Modifier
                    .size(360.dp)
                    .align(Alignment.Center)
            )
            Image(
                painter = tiges,
                "",
                modifier = Modifier
                    .size(360.dp)
                    .align(Alignment.Center)
            )
            if (plant.size == 2) {
                Image(
                    painter = fulles,
                    "",
                    modifier = Modifier
                        .size(360.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun BottomBar(updateWaterLevel: (Int) -> Unit,
              updateLeaveRecollection:(Int) -> Unit
) {
    val heightBottomBar = 192.dp
    val heightCircle = 125.dp
    val heightTotal = heightBottomBar+heightCircle/2
    val heightButton = 125.dp
    val widthButton = 140.dp

    val openDialog = remember { mutableStateOf(false)  }
    var actionString = ""

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
                        .padding(start = 8.dp)
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
                                GlobalScope.launch(Dispatchers.IO) {
                                    //CoroutineScope(Dispatchers.IO).launch {

                                    val newUpdateActionRequest = ActionRequest("prune")
                                    val call = getRetrofit()
                                        .create(APIService::class.java)
                                        .actions(
                                            "Bearer " + tokenState.token,
                                            "potus/action",
                                            newUpdateActionRequest
                                        )
                                    val body = call.body()
                                    val Ebody = call.errorBody()
                                    if (call.isSuccessful) {
                                        tokenState.signUser(body)
                                        tokenState.user?.currency?.let { updateLeaveRecollection(it) }
                                    } else {
                                        openDialog.value = true
                                        if (Ebody != null) {
                                            var jObjErr = JSONObject(Ebody.string())
                                            actionString = jObjErr.getString("message")
                                        }
                                    }
                                }
                            })
                            .padding(8.dp)
                            .size(heightButton)
                            .align(Alignment.CenterVertically))
                }
                if (openDialog.value) {
                    Toast.makeText(LocalContext.current, actionString, Toast.LENGTH_SHORT).show()
                    openDialog.value = false
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = SoothingGreen,
                    modifier = Modifier
                        .padding(end = 8.dp)
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
                                    val call = getRetrofit()
                                        .create(APIService::class.java)
                                        .actions(
                                            "Bearer " + tokenState.token,
                                            "potus/action",
                                            newUpdateActionRequest
                                        )

                                    val Ebody = call.errorBody()
                                    if (call.isSuccessful) {
                                        tokenState.signUser(call.body())
                                        tokenState.user?.potus?.let { updateWaterLevel(it.waterLevel) }
                                    } else {
                                        openDialog.value = true
                                        if (Ebody != null) {
                                            var jObjErr = JSONObject(Ebody.string())
                                            actionString = jObjErr.getString("message")
                                        }
                                    }
                                }
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

@Composable
fun PlantEvents(state:String): List<Painter> {
    when (state) {
        "C6H6" -> { return listOf(painterResource(id = R.drawable.planta_c6h6_tiges), painterResource(id = R.drawable.planta_c6h6_fulles)) }
        "Cl2" -> { return listOf(painterResource(id = R.drawable.planta_cl2_tiges), painterResource(id = R.drawable.planta_cl2_fulles)) }
        "CO" -> { return listOf(painterResource(id = R.drawable.planta_co_tiges), painterResource(id = R.drawable.planta_co_fulles)) }
        "H2S" -> { return listOf(painterResource(id = R.drawable.planta_h2s_tiges)) }
        "HCl" -> { return listOf(painterResource(id = R.drawable.planta_hcl_tiges), painterResource(id = R.drawable.planta_hcl_fulles)) }
        "HCNM" -> { return listOf(painterResource(id = R.drawable.planta_hcnm_tiges), painterResource(id = R.drawable.planta_hcnm_fulles)) }
        "HCT" -> { return listOf(painterResource(id = R.drawable.planta_hct_tiges), painterResource(id = R.drawable.planta_hct_fulles)) }
        "Hg" -> { return listOf(painterResource(id = R.drawable.planta_hg_tiges), painterResource(id = R.drawable.planta_hg_fulles)) }
        "NO2" -> { return listOf(painterResource(id = R.drawable.planta_no2_tiges)) }
        "NO" -> { return listOf(painterResource(id = R.drawable.planta_no_tiges), painterResource(id = R.drawable.planta_no_fulles)) }
        "NOX" -> { return listOf(painterResource(id = R.drawable.planta_nox_tiges), painterResource(id = R.drawable.planta_nox_fulles)) }
        "O3" -> { return listOf(painterResource(id = R.drawable.planta_o3_tiges), painterResource(id = R.drawable.planta_o3_fulles)) }
        "PM1" -> { return listOf(painterResource(id = R.drawable.planta_pm1_tiges), painterResource(id = R.drawable.planta_pm1_fulles)) }
        "PM10" -> { return listOf(painterResource(id = R.drawable.planta_pm10_tiges), painterResource(id = R.drawable.planta_pm10_fulles)) }
        "PM2_5" -> { return listOf(painterResource(id = R.drawable.planta_pm25_tiges), painterResource(id = R.drawable.planta_pm25_fulles)) }
        "PS" -> { return listOf(painterResource(id = R.drawable.planta_ps_tiges), painterResource(id = R.drawable.planta_ps_fulles)) }
        "SO2" -> { return listOf(painterResource(id = R.drawable.planta_so2_tiges), painterResource(id = R.drawable.planta_so2_fulles)) }
        else -> { return listOf(painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
    }
}

@Composable
fun ThematicEvents(event:String): Painter {
    when (event) {
        //31.12 & 01.01
        "CapAny" -> { return painterResource(id = R.drawable.test_cap_any) }
        //Dia canviant !!! (22.01 - 01.02)
        "CapAnyXines" -> { return painterResource(id = R.drawable.test_cap_any_xines) }
        //Dia canviant !!! (01.02 - 10.03)
        "MardiGras" -> { return painterResource(id = R.drawable.test_mardi_gras) }
        //17.03
        "SantPatrici" -> { return painterResource(id = R.drawable.test_sant_patrici) }
        //Dia canviant !!! (19.03 - 21.03)
        "EquinocciPrimavera" -> { return painterResource(id = R.drawable.test_equinocci_primavera) }
        //Dia canviant !!! (22.03 - 25.04)
        "Pasqua" -> { return painterResource(id = R.drawable.test_pasqua) }
        //Dia canviant !!! (20.06 - 22.06)
        "SolsticiEstiu" -> { return painterResource(id = R.drawable.test_solstici_estiu) }
        //24.06
        "SantJoan" -> { return painterResource(id = R.drawable.test_sant_joan) }
        //Dia canviant !!! (21.09 - 24.09)
        "EquinocciTardor" -> { return painterResource(id = R.drawable.test_equinocci_tardor) }
        //31.10
        "Halloween" -> { return painterResource(id = R.drawable.test_halloween) }
        //Dia canviant !!! (20.12 - 23.12)
        "SolsticiHivern" -> { return painterResource(id = R.drawable.test_solstici_hivern) }
        //25.12
        "Nadal" -> { return painterResource(id = R.drawable.test_nadal) }
        //Aniversari de l'usuari
        "Aniversari" -> { return painterResource(id = R.drawable.test_aniversari) }
        else -> { return painterResource(id = R.drawable.test_basic) }
    }
}
