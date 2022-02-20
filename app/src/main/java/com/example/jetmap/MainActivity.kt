package com.example.jetmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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

    var poi by remember {
        mutableStateOf("")
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
            poi = it.name
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
                onClick = markerClick,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
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
        CustomNaveBar(poi=poi)
    }


}

@Composable
fun CustomNaveBar(poi: String?){
    Row(modifier = Modifier.height(10.dp)) {
        Spacer(modifier = Modifier.height(10.dp))
    }
    Card(
        Modifier
            .height(75.dp)
            .fillMaxWidth()
            .padding(top = 5.dp, end = 5.dp, start = 5.dp), elevation = 4.dp, shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.padding(start = 4.dp)
            ){
                Icon(Icons.Filled.Menu,contentDescription = "menu",modifier = Modifier.fillMaxHeight())
            }
            Text(text = poi ?: "Search...", modifier = Modifier.padding(top = 15.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
            IconButton(
                onClick = {}
            ) {
                Image(
                    painterResource(id = R.drawable.ic_baseline_person_outline_24),
                    contentDescription = " ",
                    contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .clip(CircleShape)                       // clip to the circle shape
                        .border(2.dp, Color.Cyan, CircleShape)
                        .padding(4.dp)
                        .fillMaxHeight()
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetMapTheme {
        CustomNaveBar(poi = null)
    }
}