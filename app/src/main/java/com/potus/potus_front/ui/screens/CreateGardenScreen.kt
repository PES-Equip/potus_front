package com.potus.potus_front.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.CreateGardenRequest
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
/*
    val tokenState = TokenState.current
    val user = tokenState.user!!
    var newGarden = remember { mutableStateOf(Triple("NEW GARDEN", 0, "")) } */

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = 100, //user.potus.waterLevel,
            collection = 100, //user.currency,
            username = "JoeBiden", //user.username,
            addedWater = 0,
            addedLeaves = 0
        )
        Surface(color = Daffodil, modifier = Modifier.weight(1f)) {
            var newGardenName = remember { mutableStateOf("") }

            //INPUT

            /* LaunchedEffect(Dispatchers.IO) {
                val newGardenRequest = CreateGardenRequest(name = "NEW GARDEN")
                    val call = getRetrofit()
                        .create(APIService::class.java)
                        .createGarden(
                            "Bearer " + tokenState.token,
                            "gardens",
                            newGardenRequest
                        )

                    if (call.isSuccessful) {
                        //call.body()?.let { tokenState.allGardens(it.garden) }
                    } else {
                        //ERROR MESSAGES, IF ANY
                        error.value = call.code()
                        openDialog.value = true
                    }
                }*/
        }
        GardenBottomBar(painterResource(id = R.drawable.basic), painterResource(id = R.drawable.icona_seleccio_jardi))
    }
}