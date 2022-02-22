package com.example.jetmap.feature_google_places.data.remote.dto

import com.example.jetmap.feature_google_places.domain.model.Duration

data class DurationDto(
    val text: String,
    val value: Int
){
    fun toDuration(): Duration{
        return Duration(
            text = text,
            value = value
        )
    }
}
