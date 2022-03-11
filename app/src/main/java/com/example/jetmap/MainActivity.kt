package com.example.jetmap

import android.annotation.SuppressLint
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetmap.featur_typicode_users.domain.model.UserInfo
import com.example.jetmap.featur_typicode_users.presentation.UserInfoViewModel
import com.example.jetmap.feature_google_places.presentation.GooglePlacesInfoViewModel
import com.example.jetmap.ui.theme.JetMapTheme
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.google.maps.android.compose.Polyline
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

const val TAG = "MainActivityMap"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isMapLoaded by remember { mutableStateOf(false) }
            JetMapTheme {
                // A surface container using the 'background' color from the theme
                val usersInfoViewModel: UserInfoViewModel = hiltViewModel()
                val usersInfoState = usersInfoViewModel.usersInfoState.value
                val scaffoldState = rememberScaffoldState()

                val glaces: GooglePlacesInfoViewModel = hiltViewModel()
                val gPlaceInfoState = glaces.googlePlacesInfoState.value


                LaunchedEffect(key1 = true){
                    glaces.evenFlow.collectLatest { event ->
                        when(event){
                            is GooglePlacesInfoViewModel.UIEvent.ShowSnackBar ->{
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message
                                )
                            }
                        }
                    }
                    usersInfoViewModel.evenFlow.collectLatest { event ->
                        when(event){
                            is UserInfoViewModel.UIEvent.ShowSnackBar ->{
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message
                                )
                            }
                        }
                    }
                }
                Scaffold(scaffoldState = scaffoldState) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        GoogleMapView(
                            modifier = Modifier.fillMaxSize(),
                            onMapLoaded = {
                                isMapLoaded = true
                            },
                            users = usersInfoState.usersInfo,
                            googlePlacesInfoViewModel = glaces
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
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GoogleMapView(modifier: Modifier, onMapLoaded: () -> Unit, users:  List<UserInfo>, googlePlacesInfoViewModel:GooglePlacesInfoViewModel) {
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
//            it.latLng.latitude

            googlePlacesInfoViewModel.getDirection(
                origin = "${singapore.latitude}, ${singapore.longitude}",
                destination = "${it.latLng.latitude}, ${it.latLng.longitude}",
                key = MapKey.KEY
            )

//            val gPlaceInfoState = googlePlacesInfoViewModel.googlePlacesInfoState.value



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

        Polyline(points = googlePlacesInfoViewModel.polyLinesPoints.value, onClick = {
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
//    Column(modifier = Modifier.padding(10.dp))
//    {
//        CustomNaveBar(poi=poi)
//        Spacer(modifier = Modifier.height(LocalConfiguration.current.screenWidthDp.dp))
//        LazyRow {
//            items(count = users.size) { user ->
//                UserInfoRow(user = users[user], onItemClicked = {} )
//            }
//        }
//    }



}
/**
 * Stateless composable that displays a full-width [UserInfo].
 *
 * @param UserInfo item to show
 * @param modifier modifier for this element
 */
@Composable
fun UserInfoRow(
    user: UserInfo,
    onItemClicked: (UserInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable { onItemClicked(user) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(500.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp - 90.dp),
    ) {
        Column(modifier.padding(10.dp))  {
            Text(user.name)
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(user.email)
                Text(user.website)
            }
        }
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
    val users: List<UserInfo> = listOf<UserInfo>(
        UserInfo(name = "John Doe", email = "uer@g.net", phone = "123124", username = "User Name", website = "", id = 1),
        UserInfo(name = "John Doe", email = "uer@g.net", phone = "123124", username = "User Name", website = "", id = 1),
        UserInfo(name = "John Doe", email = "uer@g.net", phone = "123124", username = "User Name", website = "", id = 1)
    )
    JetMapTheme {
        users.map { user ->
            UserInfoRow(user = user, onItemClicked = {} )
        }

    }
}