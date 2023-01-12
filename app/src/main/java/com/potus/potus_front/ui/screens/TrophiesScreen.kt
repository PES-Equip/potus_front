package com.potus.potus_front.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.potus.potus_front.composables.BottomBar
import com.potus.potus_front.composables.GardenBottomBar
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen


@Preview
@Composable
// onNavigateToHome: () -> Unit, onNavigateToProfile: () -> Unit,
fun TrophiesScreen() {
    Column() {
        TopBar(
            waterLevel = 100,
            collection = 0,
            username = "Mr.Simon",
            addedWater = 0,
            addedLeaves = 0,
            onNavigateToProfile = {}
        ) {}

        Surface(
            modifier = Modifier.fillMaxWidth().weight(1f),
            color = SoothingGreen
        ) {}
    }

}