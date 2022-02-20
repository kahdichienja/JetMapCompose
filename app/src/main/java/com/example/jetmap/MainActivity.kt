package com.example.jetmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetmap.ui.theme.JetMapTheme
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.google.maps.android.compose.Polyline

private const val TAG = "MainActivityMap"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isMapLoaded by remember { mutableStateOf(false) }
            JetMapTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GoogleMapView(
                        modifier = Modifier.fillMaxSize(),
                        onMapLoaded = {
                            isMapLoaded = true
                        }
                    )
                    
                    if(!isMapLoaded){
                        AnimatedVisibility(
                            modifier = Modifier
                                .fillMaxSize(),
                            visible = !isMapLoaded,
                            enter = EnterTransition.None,
                            exit = fadeOut()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .background(MaterialTheme.colors.background)
                                    .wrapContentSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleMapView(modifier: Modifier, onMapLoaded: () -> Unit) {
    val singapore = LatLng(1.35, 103.87)
    val singapore2 = LatLng(1.40, 103.77)
    var pos by remember {
        mutableStateOf(LatLng(singapore.latitude, singapore.longitude))
    }

    val _makerList: MutableList<LatLng> =   mutableListOf<LatLng>()

    _makerList.add(singapore)
    _makerList.add(singapore2)

    var pos2 by remember {
        mutableStateOf(_makerList)
    }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 11f)
    }

    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(compassEnabled = false)
        )
    }
    var shouldAnimateZoom by remember { mutableStateOf(true) }
    var ticker by remember { mutableStateOf(0) }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        googleMapOptionsFactory = {
            GoogleMapOptions().camera(
                CameraPosition.fromLatLngZoom(
                    singapore,
                    11f
                )
            )
        },
        onMapClick = {
            Log.d(TAG, "Coordinate clicked: $it")
            pos2.add(it)
            pos = it
        },
        onPOIClick = {
            Log.d(TAG, "POI clicked: ${it.name}")
        }
    ){
        // Drawing on the map is accomplished with a child-based API
        val markerClick: (Marker) -> Boolean = {
            Log.d(TAG, "${it.title} was clicked")
            false
        }
        pos2.forEach { posistion ->

            Marker(
                position = posistion,
                title = "Singapore ",
                snippet = "Marker in Singapore ${posistion.latitude}, ${posistion.longitude}",
                onClick = markerClick
            )
        }

        Polyline(points = _makerList, onClick = {
            Log.d(TAG, "${it.points} was clicked")
        })
//        Marker(
//            position = singapore2,
//            title = "Singapore",
//            snippet = "Marker in Singapore"
//        )
//        Circle(
//            center = singapore,
//            fillColor = MaterialTheme.colors.secondary,
//            strokeColor = MaterialTheme.colors.secondaryVariant,
//            radius = 1000.0,
//        )

    }
    Column(modifier = Modifier.padding(10.dp))
    {
        Text(text = "Map Coord ${pos.toString()}")

    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetMapTheme {
        GoogleMapView(modifier = Modifier.fillMaxSize(),  onMapLoaded = {})
    }
}