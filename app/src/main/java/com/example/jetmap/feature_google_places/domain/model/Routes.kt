package com.example.jetmap.feature_google_places.domain.model

data class Routes(
    val summary: String,
    val overview_polyline: OverviewPolyline,
    val legs: List<Legs>
)
