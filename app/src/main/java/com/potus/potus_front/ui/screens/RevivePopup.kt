package com.potus.potus_front.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.SoothingGreen

@Preview
@Composable
fun RevivePopup() {
    Surface(modifier = Modifier
        .fillMaxSize(),
        color = BraveGreen
    ) {
        val sidePadding = 60.dp
        val verticalPadding = 200.dp
        Surface(modifier = Modifier
            .padding(
                start = sidePadding,
                end = sidePadding,
                top = verticalPadding,
                bottom = verticalPadding
            )
            .clip(RoundedCornerShape(46.dp)),
            color = Color.White
        )
        {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(0.2f))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Your Potus\nDied",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                    )
                Spacer(modifier = Modifier.weight(0.15f))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(all = 16.dp),
                    text = "Your Potus has died! \n\n" +
                            "Your buddy is now part of your past Potus collection. \n\n" +
                            "Now, pick a name for your next Potus!",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.weight(0.4f))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Revive your Son")
                }
                Spacer(modifier = Modifier.weight(0.1f))
            }
        }
    }
}