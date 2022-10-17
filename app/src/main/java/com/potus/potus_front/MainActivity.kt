package com.potus.potus_front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Potus_frontTheme
import com.potus.potus_front.ui.theme.SoothingGreen
//import com.potus.potus_front.composables.[composable]

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Potus_frontTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // color = MaterialTheme.colors.background
                    color = MaterialTheme.colors.background

                ) {
                    BaseApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseApp() {
    var waterLevelState by remember {mutableStateOf(0)}
    var collection by remember { mutableStateOf(0)}

    Column(Modifier.background(color = SoothingGreen)){
        TopBar(waterLevel = waterLevelState, collection = collection)
        Surface(color = SoothingGreen, modifier = Modifier.weight(1f)) {
            CenterArea()
        }
        BottomBar(
            waterLevel = waterLevelState,
            updateWaterLevel = {
                    newWaterLevel ->
                    waterLevelState = newWaterLevel
            },
            leaves = collection,
            updateLeaveRecollection = {
                    collectedLeaves ->
                    collection = collectedLeaves
            }
        )
    }
}


@Composable
fun CenterArea() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(128.dp))
        Box(modifier = Modifier
            .align(CenterHorizontally))
            {
                Image(
                    painter = painterResource(id = R.drawable.test_basic),
                    "",
                    modifier = Modifier
                        .size(360.dp)
                        .align(Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.planta_basic_tiges),
                    "",
                    modifier = Modifier
                        .size(360.dp)
                        .align(Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.planta_basic_fulles),
                    "",
                    modifier = Modifier
                        .size(360.dp)
                        .align(Center)
                )
            }
    }

}

// En el composable Top Bar caldria descobrir com separar els composables de water i leaves.
@Composable
fun TopBar(waterLevel: Int, collection: Int) {
        Row(
            Modifier
                .background(color = BraveGreen)
                .fillMaxWidth()
                .height(64.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier
                .align(CenterVertically)
                .width(64.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = Color(0x0CFFFFFF))){
                Text(modifier = Modifier
                    .align(Center),
                    text = "$waterLevel %" )
            }
            Spacer(modifier = Modifier.weight(0.02f))
            Box(modifier = Modifier
                .align(CenterVertically)
                .width(64.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = Color(0x0CFFFFFF))){
                Text(modifier = Modifier
                    .align(Center),
                    text = "$collection")
            }
            Spacer(modifier = Modifier.weight(0.075f))
        }
}

@Composable
fun BottomBar(waterLevel: Int, updateWaterLevel: (Int) -> Unit,
              leaves:Int, updateLeaveRecollection:(Int) -> Unit) {
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
                .align(BottomCenter)
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
                        .align(CenterVertically)
                        .width(widthButton)
                        .height(heightButton)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icona_podar),
                        "",
                        modifier = Modifier
                            .clickable(onClick = { updateLeaveRecollection(leaves + 1) })
                            .padding(8.dp)
                            .size(heightButton)
                            .align(CenterVertically))
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = SoothingGreen,
                    modifier = Modifier
                        .align(CenterVertically)
                        .width(widthButton)
                        .height(heightButton)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icona_regadora),
                        "",
                        modifier = Modifier
                            .clickable(onClick = { updateWaterLevel(waterLevel + 10) })
                            .size(heightButton)
                            .align(CenterVertically))
                }
            }
        }
        Surface(
            color = BraveGreen,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = (heightBottomBar - heightCircle / 2))
                .align(TopCenter)
                .size(heightCircle)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icona_jardi),
                "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(heightCircle)
                    .align(Center)
                    .clip(CircleShape)
                    .background(color = SoothingGreen))
        }
    }
}


//fun PopupDragDemo() {
//    val offset = remember { mutableStateOf(Offset.Zero) }
//
//    Column {
//        Text("That is a pop up with a dragGestureFilter on it.  You can drag it around!")
//        BoxWithConstraints(
//            alignment = Alignment.TopStart,
//            offset = offset.value.round()
//        ) {
//            Box {
//                Box(
//                    Modifier
//                        .pointerInput(Unit) {
//                            detectDragGestures { _, dragAmount ->
//                                offset.value = offset.value + dragAmount
//                            }
//                        }
//                        .size(70.dp)
//                        .background(Color.Green, CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "This is a popup!",
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//    }
//}


