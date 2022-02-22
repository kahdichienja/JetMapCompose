package com.example.jetmap.feature_google_places.data.remote.dto

import com.example.jetmap.feature_google_places.domain.model.Routes

data class RoutesDto(
    val summary: String,
    val overview_polyline: OverviewPolylineDto,
    val legs: List<LegsDto>
)
{
    fun toRoutes(): Routes{
        return Routes(
            summary = summary,
            overview_polyline = overview_polyline.toOverviewPolyline(),
            legs = legs.map { it.toLegs() }
        )
    }
}
