package com.example.jetmap.feature_google_places.data.remote.dto

import com.example.jetmap.feature_google_places.domain.model.OverviewPolyline
import com.google.android.gms.maps.model.LatLng

data class OverviewPolylineDto(
    val points: String
){
    fun toOverviewPolyline(): OverviewPolyline{
        return OverviewPolyline(
            points = points
        )
    }
}
