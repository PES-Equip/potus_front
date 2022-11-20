package com.potus.potus_front.ui.screens
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.potus.potus_front.ui.screens.Screen.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AuthScreen.route){
        composable(SwitcherScreen.route){
            ApplicationSwitcher(navController)
        }
        composable(Pantalla1.route){
            Pantalla1()
        }
        composable(HomeScreen.route){
            HomeScreen()
        }
        composable(RegisterScreen.route){
            RegisterScreen(navController)
        }
        composable(AuthScreen.route){
            AuthScreen(navController)
        }
        composable(route= ProfileScreen.route){
            ProfileScreen(navController)
        }
        composable(route = MainScreen.route){
            MainScreen(navController = navController)
        }
        composable(
            route= DetailScreen.route + "/{name}",
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType
                    defaultValue= "Philipp"
                    nullable = true
                }
            )
        ){ entry ->
            DetailScreen(name= entry.arguments?.getString("name"))
        }


    }
    
}

@Composable
fun MainScreen(navController: NavController){
    var text by remember{
        mutableStateOf("")
    }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ){
        TextField(
            value =text,
            onValueChange = {
                text = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                      navController.navigate(DetailScreen.route)

            },
            modifier = Modifier.align(Alignment.End)
        ){
            Text("To DetailScreen")
        }

    }
}

@Composable
fun DetailScreen(name: String?){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text="Hello, $name")

    }
}
