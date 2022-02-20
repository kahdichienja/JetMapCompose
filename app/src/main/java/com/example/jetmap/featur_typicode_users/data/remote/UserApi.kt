package com.example.jetmap.featur_typicode_users.data.remote

import com.example.jetmap.featur_typicode_users.data.remote.dto.UserInfoDto
import retrofit2.http.GET
//import retrofit2.http.Path

interface UserApi {
    @GET("/users")
    suspend fun getUsers(): List<UserInfoDto>


    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
//    suspend fun getDirection(
//        @Path("origin") origin: String,
//        @Path("destination") destination: String,
//    )
}