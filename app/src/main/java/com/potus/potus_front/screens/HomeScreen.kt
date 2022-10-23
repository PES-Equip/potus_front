package com.potus.potus_front.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.potus.potus_front.composables.BottomBar
import com.potus.potus_front.composables.CenterArea
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.controllers.PotusController
import com.potus.potus_front.ui.theme.SoothingGreen


@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    var waterLevelState by remember { mutableStateOf(PotusController.getWater()) }
    var collection by remember { mutableStateOf(PotusController.getLeaves()) }

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