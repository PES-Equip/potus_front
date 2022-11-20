package com.potus.potus_front

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.potus.potus_front.models.TokenState
import com.potus.potus_front.models.TokenStateViewModel
import com.potus.potus_front.ui.screens.ApplicationSwitcher
import com.potus.potus_front.ui.screens.Navigation
import com.potus.potus_front.ui.theme.Potus_frontTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenStateViewModel: TokenStateViewModel by viewModels()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        tokenStateViewModel.myLocation(getLastLocation())

        setContent {

            Potus_frontTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // color = MaterialTheme.colors.background
                    color = MaterialTheme.colors.background

                ) {

                    CompositionLocalProvider(TokenState provides tokenStateViewModel) {
                        Navigation()
                        //ApplicationSwitcher()
                        //AuthScreen()
                    }
                    //Navigation(tokenViewModel)
                }
            }
        }
    }

    private fun getLastLocation() : Pair<Double,Double> {
        //IF WE DO NOT HAVE THE USER'S PERMISSION TO ACCESS THEIR LOCATION OR THE REQUEST FAILS:
        // THE DEFAULT RETURNED LOCATION WILL BE (0,0)
        var lat = 0.0
        var lon = 0.0
        //If we do not have permission to access the user's location, ask for it
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request the user's permission
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),101)
        }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Get user's last location
            val userLocation = fusedLocationClient.lastLocation
            //Successful attempt to get user's last location
            userLocation.addOnSuccessListener {
                if(it != null) {
                    lat = it.latitude
                    lon = it.longitude
                }
                Timber.tag(tag).d("getLastLocation: latitude is %s", lat)
                Timber.tag(tag).d("getLastLocation: longitude is %s", lon)
            }
            //Failed attempt to get user's last location
            userLocation.addOnFailureListener {
                Timber.tag(tag).d("getLastLocation: Location cannot be traced")
            }
        }
        return Pair(lat, lon)
    }
}
