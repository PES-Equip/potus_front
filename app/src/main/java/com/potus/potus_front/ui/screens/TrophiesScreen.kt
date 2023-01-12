package com.potus.potus_front.ui.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.response.data_models.UserTrophy
import com.potus.potus_front.composables.TopBar
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject


@Composable
fun TrophiesScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToShop: () -> Unit
) {
    // onNavigateToHome: () -> Unit, onNavigateToProfile: () -> Unit,
    val tokenState = TokenState.current
    val user = tokenState.user?.user
    var trophies = remember { mutableStateOf(tokenState.user?.trophies) }
    val error = remember { mutableStateOf("Unknown error") }
    val openDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getTrophies(
                "Bearer " + tokenState.token,
                "user/profile/trophies"
            )

        if (call.isSuccessful) {
            if (call.body()?.size!! > 0) {
                tokenState.myTrophies(call.body()!!)
                trophies.value = tokenState.user?.trophies
            }
        } else {
            val jObjErr = call.errorBody()?.string()?.let { JSONObject(it) }
            if (jObjErr != null) {
                error.value = jObjErr.getString("message")
            }
            openDialog.value = true
        }
    }

    if (openDialog.value) {
        Toast.makeText(LocalContext.current, error.value, Toast.LENGTH_SHORT).show()
        openDialog.value = false
    }

    Column() {
        user?.let {
            TopBar(
                waterLevel = it.potus.waterLevel,
                collection = it.currency,
                username = it.username,
                addedWater = 0,
                addedLeaves = 0,
                onNavigateToProfile = { onNavigateToProfile() },
                onNavigateToShop = { onNavigateToShop() }
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SoothingGreen
        ) {
            if (trophies.value != null && trophies.value!!.isNotEmpty()) {
                TrophiesList(trophies.value!!)
            }
            else {
                Column (modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(0.05f))
                    Text(
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .weight(1f),
                        text = "No trophies earned yet!",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )
                }
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
            .clip(RoundedCornerShape(4.dp))
            .border(width = 1.dp, color = Color.Yellow),
        color = Daffodil
    ) {
        Row() {
            Icon(
                Icons.Filled.Star,
                contentDescription = "",
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(horizontal = 8.dp)
            )
            Column(modifier = Modifier.weight(0.7f)) {
                Text(
                    text = trophy.trophy.name,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                var level: Int? = 0
                if(trophy.level!! > 0) {
                    level = trophy.level
                }
                val nextLevel = trophy.next_level

                Text(
                    text = "Level: $level | $nextLevel until next level!",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
