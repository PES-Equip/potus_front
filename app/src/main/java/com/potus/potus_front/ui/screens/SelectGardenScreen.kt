package com.potus.potus_front.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.InformLocationRequest
import com.potus.potus_front.API.requests.RegisterUserRequest
import com.potus.potus_front.composables.*
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.Dispatchers


@Composable
@Preview
fun SelectGardenScreen() {
    val openDialog = remember { mutableStateOf(false)  }
    val error = remember { mutableStateOf(200)  }

    val tokenState = TokenState.current
    val user = tokenState.user!!
    var gardenList by remember { mutableStateOf(listOf(Triple("", 0, ""))) }

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getGardenList(
                "Bearer " + tokenState.token,
                "gardens"
            )

        if (call.isSuccessful) {
            call.body()?.let { tokenState.allGardens(it.gardens) }
            gardenList = tokenState.gardens
        } else {
            //ERROR MESSAGES, IF ANY
            error.value = call.code()
            openDialog.value = true
        }
    }

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = user.potus.waterLevel,
            collection = user.currency,
            username = user.username,
            addedWater = 0,
            addedLeaves = 0
        )
        Surface(color = Daffodil, modifier = Modifier.weight(1f)) {
            GardenList(gardenList)
        }
        //Spacer(modifier = Modifier.height(612.dp))
        GardenBottomBar()
    }
}

@Composable
fun GardenList (gardens: List<Triple<String, Int, String>>) {
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(SoothingGreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = "This Is Item Number $number", color = BraveGreen)
    }
}