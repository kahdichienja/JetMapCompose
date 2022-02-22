package com.example.jetmap.feature_google_places.di

import com.example.jetmap.feature_google_places.data.remote.GooglePlacesApi
import com.example.jetmap.feature_google_places.data.repository.GooglePlacesInfoRepositoryImplementation
import com.example.jetmap.feature_google_places.domain.repository.GooglePlacesInfoRepository
import com.example.jetmap.feature_google_places.domain.use_case.GetDirectionInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GooglePlacesModule {

    @Provides
    @Singleton
    fun provideGetDirectionInfo(repository: GooglePlacesInfoRepository): GetDirectionInfo{
        return GetDirectionInfo(repository = repository)
    }

    @Provides
    @Singleton
    fun provideDirectionInfoRepository(api: GooglePlacesApi): GooglePlacesInfoRepository{
        return GooglePlacesInfoRepositoryImplementation(api = api)
    }

    @Provides
    @Singleton
    fun provideGooglePlacesApi(): GooglePlacesApi{
        return Retrofit.Builder()
            .baseUrl(GooglePlacesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GooglePlacesApi::class.java)
    }
}