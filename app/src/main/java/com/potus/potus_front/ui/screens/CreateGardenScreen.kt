package com.potus.potus_front.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.InformLocationRequest
import com.potus.potus_front.API.requests.RegisterUserRequest
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.Dispatchers


@Preview
@Composable
fun CreateGardenScreen() {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    //val tokenState = TokenState.current
    //val user = tokenState.user!!

    /*LaunchedEffect(Dispatchers.IO) {
    val newUpdateStateRequest = InformLocationRequest(latitude = tokenState.location.first, length = tokenState.location.second)
        val call = getRetrofit()
            .create(APIService::class.java)
            .getGardenList(
                "Bearer " + tokenState.token,
                "gardens"
             newUpdateStateRequest
            )

        if (call.isSuccessful) {
            call.body()?.let { tokenState.allGardens(it.gardens) }
        } else {
            //ERROR MESSAGES, IF ANY
            error.value = call.code()
            openDialog.value = true
        }
    }*/

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = 100, //user.potus.waterLevel,
            collection = 100, //user.currency,
            username = "JoeBiden", //user.username,
            addedWater = 0,
            addedLeaves = 0
        )
        Surface(color = Daffodil, modifier = Modifier.weight(1f)) {
            List(listOf(Triple("NO GARDENS", 0, "You dumb fuck.")))
        }
        GardenBottomBar(painterResource(id = R.drawable.basic), painterResource(id = R.drawable.icona_nou_jardi))
    }
}

@Composable
fun List (gardens: List<Triple<String, Int, String>>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gardens.size) {
                arrayItem -> ColumnItem(number = arrayItem)
        }
    }
}

@Composable
fun ColumnItem(number: Int) {
    var toggled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SoothingGreen)
            .toggleable(value = toggled, onValueChange = { toggled = it })
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!toggled) Text(text = "NO GARDENS", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BraveGreen)
        else {
            Text(text = "NO GARDENS\n\n Members: 0\n\nAbout: You dumb fuck.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BraveGreen)
            Surface(
                color = BraveGreen,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(64.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) { Text(text = "Join!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Daffodil) }
        }
    }
}