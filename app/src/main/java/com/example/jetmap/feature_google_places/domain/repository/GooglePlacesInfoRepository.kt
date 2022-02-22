package com.example.jetmap.feature_google_places.domain.repository

import com.example.jetmap.core.util.Resource
import com.example.jetmap.feature_google_places.domain.model.GooglePlacesInfo
import kotlinx.coroutines.flow.Flow

interface GooglePlacesInfoRepository {
    fun getDirection(origin: String, destination: String, key: String): Flow<Resource<GooglePlacesInfo>>
}