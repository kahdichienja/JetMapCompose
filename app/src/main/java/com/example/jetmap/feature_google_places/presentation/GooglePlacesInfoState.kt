package com.example.jetmap.feature_google_places.presentation

import com.example.jetmap.feature_google_places.domain.model.GooglePlacesInfo

data class GooglePlacesInfoState(val direction: GooglePlacesInfo?, val isLoading: Boolean = false)