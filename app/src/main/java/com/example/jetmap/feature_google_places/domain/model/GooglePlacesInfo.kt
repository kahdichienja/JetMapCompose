package com.example.jetmap.feature_google_places.domain.model

data class GooglePlacesInfo(
    val geocoded_waypoints: List<GeocodedWaypoints>,
    val routes: List<Routes>,
    val status: String
)
