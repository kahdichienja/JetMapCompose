package com.example.jetmap.featur_typicode_users.domain.use_case

import com.example.jetmap.core.util.Resource
import com.example.jetmap.featur_typicode_users.domain.model.UserInfo
import com.example.jetmap.featur_typicode_users.domain.repository.UserInfoRepository
import kotlinx.coroutines.flow.Flow

class GetUsersInfo(private val repository: UserInfoRepository) {
    operator fun invoke(): Flow<Resource<List<UserInfo>>> = repository.getUsersInfo()

}