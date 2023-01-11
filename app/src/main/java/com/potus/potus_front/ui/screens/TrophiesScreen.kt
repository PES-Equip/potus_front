package com.potus.potus_front.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.response.data_models.Trophy
import com.potus.potus_front.API.response.data_models.UserTrophy
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun TrophiesScreen(onNavigateToProfile: () -> Unit) {
    // onNavigateToHome: () -> Unit, onNavigateToProfile: () -> Unit,
    val tokenState = TokenState.current
    val user = tokenState.user?.user
    val trophies = tokenState.user?.trophies
    Column() {
        user?.let {
            TopBar(
                waterLevel = it.potus.waterLevel,
                collection = it.currency,
                username = it.username,
                addedWater = 0,
                addedLeaves = 0,
                onNavigateToProfile = { onNavigateToProfile() }
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SoothingGreen
        ) {
            if (trophies != null) TrophiesList(trophies)
            else {
                
            }
        }
    }

}

@Composable
fun TrophiesList(trophies: List<UserTrophy>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(trophies.size) {
                arrayItem -> TrophyItem(trophy = trophies[arrayItem])
        }
    }
}

@Composable
fun TrophyItem(trophy: UserTrophy) {
    Surface(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Yellow),
        color = Daffodil
    ) {
        Row() {
            Icon(Icons.Filled.Star, contentDescription = "")
            Column() {
                Text(text = trophy.trophy.name, fontSize = 10.sp, color = Color.DarkGray)
                val date = trophy.updateDate
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(date)
                Text(text = "Earned on : $formattedDate")
            }
        }
    }
}
