package com.potus.potus_front.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potus.potus_front.API.APIService
import com.potus.potus_front.API.getRetrofit
import com.potus.potus_front.API.requests.ChangeMemberRoleRequest
import com.potus.potus_front.API.response.data_models.SimplifiedGardenMember
import com.potus.potus_front.R
import com.potus.potus_front.composables.*
import com.potus.potus_front.google.models.TokenState
import com.potus.potus_front.ui.theme.BraveGreen
import com.potus.potus_front.ui.theme.Daffodil
import com.potus.potus_front.ui.theme.RoseRed
import com.potus.potus_front.ui.theme.SoothingGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.random.Random


@Composable
fun GardenScreen(onNavigateToProfile: () -> Unit, onNavigateToShop: () -> Unit, onNavigateToManagement: () -> Unit, onNavigateToGarden: () -> Unit, onNavigateToMeetings: () -> Unit, onNavigateToHome: () -> Unit, onNavigateToChat: () -> Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    var actionString = ""

    val tokenState = TokenState.current
    val user = tokenState.user!!.user
    val members = remember { mutableStateOf(setOf(SimplifiedGardenMember("THERE ARE NO USERS IN THIS GARDEN", "OWNER"))) }

    LaunchedEffect(Dispatchers.IO) {
        val call = getRetrofit()
            .create(APIService::class.java)
            .getGardenMembers(
                "Bearer " + tokenState.token,
                "gardens/profile/members"
            )

        val eBody = call.errorBody()
        if (call.isSuccessful) {
            call.body()?.let { members.value = it }
        } else {
            //ERROR MESSAGES, IF ANY
            openDialog.value = true
            if (eBody != null) {
                actionString = JSONObject(eBody.string()).getString("message")
            }
        }
    }

    Column(Modifier.background(color = Daffodil)) {
        TopBar(
            waterLevel = user.potus.waterLevel,
            collection = user.currency,
            username = user.username,
            addedWater = 0,
            addedLeaves = 0,
            onNavigateToProfile = { onNavigateToProfile() },
            onNavigateToShop = { onNavigateToShop() }
        )
        Column(modifier = Modifier.weight(1f).background(Daffodil)) {
            Spacer(modifier = Modifier.size(8.dp))
            Surface(modifier = Modifier.fillMaxWidth().clickable(onClick = { onNavigateToManagement() }), color = Color.Transparent) {
                Text(
                    text = tokenState.user?.user?.garden_info?.garden?.name.toString(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = BraveGreen,
                    textAlign = TextAlign.Center
                )
            }
            MembersList(members.value, onNavigateToGarden)
        }
        if (openDialog.value) {
            Toast.makeText(LocalContext.current, actionString, Toast.LENGTH_SHORT).show()
            openDialog.value = false
        }
        GardenBottomBar(painterResource(id = R.drawable.icona_meetings), onNavigateToMeetings, painterResource(id = R.drawable.basic), onNavigateToHome, painterResource(id = R.drawable.icona_xat), onNavigateToChat)
    }
}

@Composable
fun MembersList (members: Set<SimplifiedGardenMember>, onNavigateToGarden: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(members.size) {
                arrayItem -> MemberItem(member = members.elementAt(arrayItem), onNavigateToGarden)
        }
    }
}

@Composable
fun MemberItem(member: SimplifiedGardenMember, onNavigateToGarden: () -> Unit) {
    val gases = arrayOf("C6H6", "Cl2", "CO", "H2S", "HCl", "HCNM", "HCT", "Hg", "NO2", "NO", "NOX", "O3", "PM1", "PM2_5", "PM10", "PS", "SO2")
    var toggled by remember { mutableStateOf(false) }
    val randomAvatar = gases[Random.nextInt(gases.size)]

    val popUpContext = LocalContext.current

    val tokenState = TokenState.current
    val garden = tokenState.user!!.user.garden_info!!.garden.name
    val ownRole = tokenState.user!!.user.garden_info!!.role
    val username = member.username
    val role =  if (member.role != "NORMAL") member.role
                else "MEMBER"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .defaultMinSize(minHeight = 64.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SoothingGreen)
            .toggleable(value = toggled, onValueChange = { toggled = it })
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Row(modifier = Modifier.align(Start)) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .align(CenterVertically)
                    .padding(start = 8.dp)
            ) { CenterArea(randomAvatar, false) }
            if (!toggled) {
                Text(
                    text = username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BraveGreen,
                    modifier = Modifier.padding(start = 8.dp).align(CenterVertically)
                )
            }
            else {
                Surface (modifier = Modifier.fillMaxWidth(), color = Color.Transparent) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Start
                    ) {
                        Text(
                            text = "\n$username\n\nRole: $role\n",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )

                        if (tokenState.user!!.user.username != username) {
                            if (ownRole != "MEMBER") {
                                if (role != "MEMBER" && role != "OWNER") {
                                    Button(
                                        onClick = {
                                            val builder =
                                                android.app.AlertDialog.Builder(popUpContext)
                                            builder.setTitle("DEMOTE TO MEMBER")
                                            builder.setMessage("Are you sure you want to demote this user to MEMBER?")
                                            builder.setPositiveButton("DEMOTE") { dialog, which ->
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    val newChangeMemberRoleRequest =
                                                        ChangeMemberRoleRequest(role = "NORMAL")
                                                    getRetrofit()
                                                        .create(APIService::class.java)
                                                        .changeMemberRole(
                                                            "Bearer " + tokenState.token,
                                                            "gardens/$garden/$username",
                                                            garden = garden,
                                                            user = username,
                                                            requestModel = newChangeMemberRoleRequest
                                                        )
                                                }
                                                onNavigateToGarden()
                                            }
                                            builder.setNegativeButton("CANCEL") { dialog, which ->
                                                dialog.dismiss()
                                            }
                                            val dialog = builder.create()
                                            dialog.show()
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Daffodil),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Text(text = "TURN MEMBER", color = BraveGreen)
                                    }
                                }
                                if (role != "ADMIN" && role != "OWNER") {
                                    Button(
                                        onClick = {
                                            val builder =
                                                android.app.AlertDialog.Builder(popUpContext)
                                            builder.setTitle("TURN ADMIN")
                                            builder.setMessage("Are you sure you want to make this user an ADMIN?")
                                            builder.setPositiveButton("PROMOTE") { dialog, which ->
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    val newChangeMemberRoleRequest =
                                                        ChangeMemberRoleRequest(role = "ADMIN")
                                                    getRetrofit()
                                                        .create(APIService::class.java)
                                                        .changeMemberRole(
                                                            "Bearer " + tokenState.token,
                                                            "gardens/$garden/$username",
                                                            garden = garden,
                                                            user = username,
                                                            requestModel = newChangeMemberRoleRequest
                                                        )
                                                }
                                                onNavigateToGarden()
                                            }
                                            builder.setNegativeButton("CANCEL") { dialog, which ->
                                                dialog.dismiss()
                                            }
                                            val dialog = builder.create()
                                            dialog.show()
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = BraveGreen),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Text(text = "TURN ADMIN", color = Daffodil)
                                    }
                                }
                                if (ownRole == "OWNER") {
                                    if (role != "OWNER") {
                                        Button(
                                            onClick = {
                                                val builder = android.app.AlertDialog.Builder(popUpContext)
                                                builder.setTitle("GIVE OWNERSHIP")
                                                builder.setMessage("Are you sure you want to give up this Garden's ownership?")
                                                builder.setPositiveButton("CEDE") { dialog, which ->
                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        val newChangeMemberRoleRequest =
                                                            ChangeMemberRoleRequest(role = "OWNER")
                                                        getRetrofit()
                                                            .create(APIService::class.java)
                                                            .changeMemberRole(
                                                                "Bearer " + tokenState.token,
                                                                "gardens/$garden/$username",
                                                                garden = garden,
                                                                user = username,
                                                                requestModel = newChangeMemberRoleRequest
                                                            )
                                                    }
                                                    onNavigateToGarden()
                                                }
                                                builder.setNegativeButton("KEEP") { dialog, which ->
                                                    dialog.dismiss()
                                                }
                                                val dialog = builder.create()
                                                dialog.show()
                                            },
                                            colors = ButtonDefaults.buttonColors(backgroundColor = RoseRed),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Text(text = "TURN OWNER", color = Daffodil)
                                        }

                                        Button(
                                            onClick = {
                                                val builder =
                                                    android.app.AlertDialog.Builder(popUpContext)
                                                builder.setTitle("REMOVE FROM GARDEN")
                                                builder.setMessage("Are you sure you want to remove this user from the Garden?")
                                                builder.setPositiveButton("REMOVE") { dialog, which ->
                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        getRetrofit()
                                                            .create(APIService::class.java)
                                                            .removeGardenMember(
                                                                "Bearer " + tokenState.token,
                                                                "gardens/$garden/$username",
                                                                garden = garden,
                                                                user = username
                                                            )
                                                    }
                                                    onNavigateToGarden()
                                                }
                                                builder.setNegativeButton("CANCEL") { dialog, which ->
                                                    dialog.dismiss()
                                                }
                                                val dialog = builder.create()
                                                dialog.show()
                                            },
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Text(text = "REMOVE FROM GARDEN", color = Color.Red)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}