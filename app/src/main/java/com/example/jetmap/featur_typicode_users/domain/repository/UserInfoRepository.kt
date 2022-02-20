package com.example.jetmap.featur_typicode_users.domain.repository

import com.example.jetmap.core.util.Resource
import com.example.jetmap.featur_typicode_users.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun getUsersInfo(): Flow<Resource<List<UserInfo>>>
}