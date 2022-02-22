package com.example.jetmap.feature_google_places.domain.model

data class GeocodedWaypoints(
    val geocoder_status: String,
    val place_id: String,
    val types: List<String>
)
