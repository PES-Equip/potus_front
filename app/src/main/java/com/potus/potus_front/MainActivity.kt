package com.potus.potus_front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Potus_frontTheme
import com.potus.potus_front.ui.theme.SoothingGreen

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
    Column(Modifier.background(color = SoothingGreen)){
        TopBar()
        Surface(color = SoothingGreen, modifier = Modifier.weight(1f)) {
            CenterArea()
        }
        BottomBar()
    }
}


@Composable
fun CenterArea() {
    Column() {
        Image(painter = painterResource(id = R.drawable.basic), "",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp))
                // Clip image to be shaped as a circle
                //.clip(CircleShape))
    }
}

@Composable
fun TopBar() {
    Surface(color = BraveGreen, modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)) {
        DropdownMenu(expanded = false, onDismissRequest = { /*TODO*/ }) {
            
        }
    }
}

@Composable
fun BottomBar() {
    Surface(color = BraveGreen, modifier = Modifier
        .fillMaxWidth()
        .height(128.dp)) {}
}
