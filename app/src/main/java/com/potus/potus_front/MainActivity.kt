package com.potus.potus_front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.composables.BottomBar
import com.potus.potus_front.composables.CenterArea
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.ui.theme.Potus_frontTheme
import com.potus.potus_front.ui.theme.SoothingGreen
import com.potus.potus_front.controllers.PotusController
import com.potus.potus_front.data.remote.PostsService
import com.potus.potus_front.data.remote.dto.PostResponse

class MainActivity : ComponentActivity() {
    //HTTP client creation
    private val service = PostsService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val posts = produceState<List<PostResponse>>(
                initialValue = emptyList(),
                producer = {
                    value = service.getPosts()
                }
            )

            Potus_frontTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BaseApp()

                    LazyColumn {
                        items(posts.value) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(text = it.title, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = it.body, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseApp() {
    var waterLevelState by remember {mutableStateOf(PotusController.getWater())}
    var collection by remember { mutableStateOf(PotusController.getLeaves())}

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