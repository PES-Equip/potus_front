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
import com.potus.potus_front.controllers.PotusController
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.ui.theme.SoothingGreen


@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    var waterLevelState by remember { mutableStateOf(PotusController.getWater()) }
    var collection by remember { mutableStateOf(PotusController.getLeaves()) }
    val user = TokenState.current.user!!

    Column(Modifier.background(color = SoothingGreen)){
        TopBar(waterLevel = user.potus.waterLevel, collection = user.currency, username = user.username)
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