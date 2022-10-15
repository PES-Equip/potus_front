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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(128.dp))
        Image(painter = painterResource(id = R.drawable.basic), "",
            modifier = Modifier
                .size(360.dp)
                .align(CenterHorizontally))
    }

}

// En el composable Top Bar caldria descobrir com separar els composables de water i leaves.
@Composable
fun TopBar() {
        Row(
            Modifier
                .background(color = BraveGreen)
                .fillMaxWidth()
                .height(64.dp)) {
            //DropdownMenu(expanded = false, onDismissRequest = { /*TODO*/ }) {}
            Spacer(modifier = Modifier.weight(1f));
            Box(modifier = Modifier
                .align(CenterVertically)
                .width(64.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = Color(0xCCFFFFFF))){
                Text(modifier = Modifier
                    .align(Center),
                    text = "water")
            }
            Spacer(modifier = Modifier.weight(0.02f));
            Box(modifier = Modifier
                .align(CenterVertically)
                .width(64.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = Color(0xCCFFFFFF))){
                Text(modifier = Modifier
                    .align(Center),
                    text = "leaves")
            }
            Spacer(modifier = Modifier.weight(0.075f));
        }
}

@Composable
fun BottomBar() {
    Surface(color = BraveGreen, modifier = Modifier
        .fillMaxWidth()
        .height(128.dp)) {

    }
}
