package com.potus.potus_front.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.potus.potus_front.composables.BottomBar
import com.potus.potus_front.composables.CenterArea
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.SoothingGreen


@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    val user = TokenState.current.user!!
    var waterLevelState by remember { mutableStateOf(user.potus.waterLevel) }
    var collection by remember { mutableStateOf(user.currency) }
    var addedWater by remember { mutableStateOf(0) }
    var addedLeaves by remember { mutableStateOf(0) }

    Column(Modifier.background(color = SoothingGreen)) {
        TopBar(
            waterLevel = waterLevelState,
            collection = collection,
            username = user.username,
            addedWater = addedWater,
            addedLeaves = addedLeaves
        )
        Surface(color = SoothingGreen, modifier = Modifier.weight(1f)) {
            CenterArea()
        }
        BottomBar(
            updateWaterLevel = { newWaterLevel ->
                addedWater = newWaterLevel - waterLevelState
                waterLevelState = newWaterLevel
            },
            updateLeaveRecollection = { collectedLeaves ->
                addedLeaves = collectedLeaves - collection
                collection = collectedLeaves
            })
    }
}