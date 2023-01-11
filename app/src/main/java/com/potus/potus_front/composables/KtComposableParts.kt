package com.potus.potus_front.composables

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.ActionRequest
import com.potus.potus_front.R
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.*
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.math.roundToInt

@Composable
fun TopBar(
    waterLevel: Int,
    collection: Int,
    username: String,
    addedWater: Int,
    addedLeaves: Int,
    onNavigateToProfile: () -> Unit
) {
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
            ClickableText(
                modifier = Modifier
                    .align(Alignment.Center),
                text = AnnotatedString(username),
                onClick = {
                    onNavigateToProfile()
                })
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GasesWindow(onToggleText: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getGases(
                "Bearer " + tokenState.token,
                "airquality/region",
                latitude = tokenState.location.first,
                length = tokenState.location.second
            )

        if (call.isSuccessful) {
            tokenState.regionalGases(call.body())
        } else {
            //ERROR MESSAGES, IF ANY
            error.value = call.code()
            openDialog.value = true
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        //var gases = arrayOf("C6H6", "Cl2", "CO", "H2S", "HCl", "HCNM", "HCT", "Hg", "NO2", "NO", "NOX", "O3", "PM1", "PM2_5", "PM10", "PS", "SO2")
        val gasesInfo = tokenState.gases.registry
        var gases = gasesInfo.keys
        var toggled by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(32.dp))
        LazyVerticalGrid(
            modifier = Modifier
                .align(CenterHorizontally)
                .width(360.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Daffodil)
                .toggleable(value = toggled, onValueChange = {
                    toggled = it
                    onToggleText()
                })
                .animateContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center,
            cells = GridCells.Fixed(4)) {
                var numberOfCells = 4
                if (toggled or (gasesInfo.size < 4)) numberOfCells = gasesInfo.size
                items(count = numberOfCells) {
                Row(
                    modifier = Modifier
                        .width(16.dp)
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val gas = gasesInfo[gases.elementAt(it)]
                    var color = Color.Gray
                    when (gas?.dangerLevel) {
                        "NoDanger" -> color = noDanger
                        "Low" -> color = Low
                        "Moderate" -> color = Moderate
                        "High" -> color = High
                        "Hazardous" -> color = Hazardous
                    }
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = gas!!.name,
                            textAlign = Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                        Text(
                            text = ((gas.value * 100.0).roundToInt()/100.0).toString() + " " + gas.unit,
                            textAlign = Center,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    }
                }
            }
        }
    }
}

@Composable
//fun CenterArea(thematicEvent:String, plantState:String) {
fun CenterArea(plantState:String, textDisplay:Boolean) {
    //val plant = PlantEvents(plantState)
    //val test = ThematicEvents(thematicEvent)
    val overallState = JoinedEvents(plantState)
    val test = overallState[0]
    val tiges = overallState[1]
    var fulles = painterResource(id = R.drawable.planta_basic_fulles)
    if (overallState.size == 3) fulles = overallState[2]
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .weight(0.8f))
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
            if (overallState.size == 3) {
                Image(
                    painter = fulles,
                    "",
                    modifier = Modifier
                        .size(360.dp)
                        .align(Alignment.Center)
                )
            }
        }
        if(textDisplay){
            Box(modifier = Modifier.weight(0.125f)){
                Text(
                    modifier = Modifier.align(BottomCenter),
                    text = TokenState.current.user?.user?.potus?.name.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = BraveGreen,
                    textAlign = Center
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.075f))
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun BottomBar(
    updateWaterLevel: (Int) -> Unit,
    updateLeaveRecollection:(Int) -> Unit,
    onNavigateToGarden: () -> Unit,
    onNavigateToSelection: () -> Unit
) {
    val heightBottomBar = 192.dp
    val heightCircle = 125.dp
    val heightTotal = heightBottomBar+heightCircle/2
    val heightButton = (heightBottomBar.value * 0.7).dp

    val openDialog = remember { mutableStateOf(false)  }
    var actionString = ""

    val tokenState = TokenState.current
    val user = tokenState.user!!.user

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
                        .weight(0.25f)
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
                                        tokenState.user?.user?.currency?.let {
                                            updateLeaveRecollection(
                                                it
                                            )
                                        }
                                    } else {
                                        openDialog.value = true
                                        if (Ebody != null) {
                                            val jObjErr = JSONObject(Ebody.string())
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
                Spacer(modifier = Modifier.weight(0.25f))
                Surface(
                    color = SoothingGreen,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically)
                        .weight(0.25f)
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

                                    val ebody = call.errorBody()
                                    if (call.isSuccessful) {
                                        tokenState.signUser(call.body())
                                        tokenState.user?.user?.potus?.let { updateWaterLevel(it.waterLevel) }
                                    } else {
                                        openDialog.value = true
                                        if (ebody != null) {
                                            val jObjErr = JSONObject(ebody.string())
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
                    .clickable(onClick = {

                        CoroutineScope(Dispatchers.IO).launch {
                            val call = getRetrofit()
                                .create(APIService::class.java)
                                .getUser(
                                    "Bearer " + tokenState.token,
                                    "user/profile"
                                )

                            if (call.isSuccessful) {
                                tokenState.signUser(call.body())
                            }
                        }

                        if (user.garden_info != null) onNavigateToGarden()
                        else onNavigateToSelection()
                    })
                    .padding(8.dp)
                    .size(heightCircle)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(color = SoothingGreen))
        }
    }
}

@Composable
fun GardenBottomBar(
    leftImage: Painter,
    onNavigateToLeft : () -> Unit,
    centerImage: Painter,
    onNavigateToCenter : () -> Unit,
    rightImage: Painter,
    onNavigateToRight : () -> Unit
) {
    val heightBottomBar = 96.dp
    val heightCircle = 160.dp
    val heightTotal = heightBottomBar+heightCircle/2
    val heightButton = 80.dp
    val widthButton = 96.dp

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(heightTotal)) {
            BoxWithConstraints(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(heightBottomBar)
                    .background(BraveGreen)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTotal)
                ) {
                    Surface(
                        color = SoothingGreen,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(0.3f)
                            .height(heightButton)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Image(
                            painter = leftImage,
                            "",
                            modifier = Modifier
                                .clickable(onClick = { onNavigateToLeft() })
                                .padding(8.dp)
                                .size(heightButton)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.5f))
                    Surface(
                        color = SoothingGreen,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(0.3f)
                            .height(heightButton)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Image(
                            painter = rightImage,
                            "",
                            modifier = Modifier
                                .clickable(onClick = { onNavigateToRight() })
                                .padding(8.dp)
                                .size(heightButton)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            Surface(
                color = BraveGreen,
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        bottom = (heightBottomBar - heightCircle / 2)
                    )
                    .align(Alignment.TopCenter)
                    .size(heightCircle)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = centerImage,
                    "",
                    modifier = Modifier
                        .clickable(onClick = { onNavigateToCenter() })
                        .padding(8.dp)
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(color = SoothingGreen)
                        )
            }
        }
}

@Composable
fun JoinedEvents(state:String): List<Painter> {
    // AS IMPLEMENTED IN THE BACKEND, PLANT EVENTS AND THEMATIC EVENTS ARE EXCLUSIVE OF EACH OTHER
    when (state) {
        "C6H6" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_c6h6_tiges), painterResource(id = R.drawable.planta_c6h6_fulles)) }
        "Cl2" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_cl2_tiges), painterResource(id = R.drawable.planta_cl2_fulles)) }
        "CO" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_co_tiges), painterResource(id = R.drawable.planta_co_fulles)) }
        "H2S" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_h2s_tiges)) }
        "HCl" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_hcl_tiges), painterResource(id = R.drawable.planta_hcl_fulles)) }
        "HCNM" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_hcnm_tiges), painterResource(id = R.drawable.planta_hcnm_fulles)) }
        "HCT" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_hct_tiges), painterResource(id = R.drawable.planta_hct_fulles)) }
        "Hg" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_hg_tiges), painterResource(id = R.drawable.planta_hg_fulles)) }
        "NO2" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_no2_tiges)) }
        "NO" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_no_tiges), painterResource(id = R.drawable.planta_no_fulles)) }
        "NOX" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_nox_tiges), painterResource(id = R.drawable.planta_nox_fulles)) }
        "O3" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_o3_tiges), painterResource(id = R.drawable.planta_o3_fulles)) }
        "PM1" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_pm1_tiges), painterResource(id = R.drawable.planta_pm1_fulles)) }
        "PM10" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_pm10_tiges), painterResource(id = R.drawable.planta_pm10_fulles)) }
        "PM2_5" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_pm25_tiges), painterResource(id = R.drawable.planta_pm25_fulles)) }
        "PS" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_ps_tiges), painterResource(id = R.drawable.planta_ps_fulles)) }
        "SO2" -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_so2_tiges), painterResource(id = R.drawable.planta_so2_fulles)) }
        "NEW_YEAR" -> { return listOf(painterResource(id = R.drawable.test_cap_any), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "CHINESE_NEW_YEAR" -> { return listOf(painterResource(id = R.drawable.test_cap_any_xines), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "MARDI_GRAS" -> { return listOf(painterResource(id = R.drawable.test_mardi_gras), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "SAINT_PATRICK" -> { return listOf(painterResource(id = R.drawable.test_sant_patrici), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "SPRING_EQUINOX" -> { return listOf(painterResource(id = R.drawable.test_equinocci_primavera), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "EASTER" -> { return listOf(painterResource(id = R.drawable.test_pasqua), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "SUMMER_SOLSTICE" -> { return listOf(painterResource(id = R.drawable.test_solstici_estiu), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "SANT_JOAN" -> { return listOf(painterResource(id = R.drawable.test_sant_joan), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "AUTUMN_EQUINOX" -> { return listOf(painterResource(id = R.drawable.test_equinocci_tardor), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "HALLOWEEN" -> { return listOf(painterResource(id = R.drawable.test_halloween), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "WINTER_SOLSTICE" -> { return listOf(painterResource(id = R.drawable.test_solstici_hivern), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "CHRISTMAS" -> { return listOf(painterResource(id = R.drawable.test_nadal), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        "BIRTHDAY" -> { return listOf(painterResource(id = R.drawable.test_aniversari), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
        else -> { return listOf(painterResource(id = R.drawable.test_basic), painterResource(id = R.drawable.planta_basic_tiges), painterResource(id = R.drawable.planta_basic_fulles)) }
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
        "NEW_YEAR" -> { return painterResource(id = R.drawable.test_cap_any) }
        //Dia canviant !!! (22.01 - 01.02)
        "CHINESE_NEW_YEAR" -> { return painterResource(id = R.drawable.test_cap_any_xines) }
        //Dia canviant !!! (01.02 - 10.03)
        "MARDI_GRAS" -> { return painterResource(id = R.drawable.test_mardi_gras) }
        //17.03
        "SAINT_PATRICK" -> { return painterResource(id = R.drawable.test_sant_patrici) }
        //Dia canviant !!! (19.03 - 21.03)
        "SPRING_EQUINOX" -> { return painterResource(id = R.drawable.test_equinocci_primavera) }
        //Dia canviant !!! (22.03 - 25.04)
        "EASTER" -> { return painterResource(id = R.drawable.test_pasqua) }
        //Dia canviant !!! (20.06 - 22.06)
        "SUMMER_SOLSTICE" -> { return painterResource(id = R.drawable.test_solstici_estiu) }
        //24.06
        "SANT_JOAN" -> { return painterResource(id = R.drawable.test_sant_joan) }
        //Dia canviant !!! (21.09 - 24.09)
        "AUTUMN_EQUINOX" -> { return painterResource(id = R.drawable.test_equinocci_tardor) }
        //31.10
        "HALLOWEEN" -> { return painterResource(id = R.drawable.test_halloween) }
        //Dia canviant !!! (20.12 - 23.12)
        "WINTER_SOLSTICE" -> { return painterResource(id = R.drawable.test_solstici_hivern) }
        //25.12
        "CHRISTMAS" -> { return painterResource(id = R.drawable.test_nadal) }
        //Aniversari de l'usuari
        "BIRTHDAY" -> { return painterResource(id = R.drawable.test_aniversari) }
        else -> { return painterResource(id = R.drawable.test_basic) }
    }
}
