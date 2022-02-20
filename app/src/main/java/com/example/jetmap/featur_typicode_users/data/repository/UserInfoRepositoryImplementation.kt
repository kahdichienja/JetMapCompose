package com.example.jetmap.featur_typicode_users.data.repository

import com.example.jetmap.core.util.Resource
import com.example.jetmap.featur_typicode_users.data.local.UserInfoDao
import com.example.jetmap.featur_typicode_users.data.remote.UserApi
import com.example.jetmap.featur_typicode_users.domain.model.UserInfo
import com.example.jetmap.featur_typicode_users.domain.repository.UserInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class UserInfoRepositoryImplementation(
    private val api: UserApi,
    private val dao: UserInfoDao,
): UserInfoRepository {
    override fun getUsersInfo(): Flow<Resource<List<UserInfo>>> = flow{
        emit(Resource.Loading())
        val usersInfo = dao.getUsersInfo().map { it.toUserInfo() }
        emit(Resource.Loading(data = usersInfo))

        try {
            val remoteUsersInfo = api.getUsers()
            dao.deleteUserInf(remoteUsersInfo.map { it.username })
            dao.insertUserInfo(remoteUsersInfo.map { it.toUserInfoEntity() })

        }catch (e: HttpException){
            emit(Resource.Error(message = "Unable To Connect To Internet", data = usersInfo))
        }catch (e: IOException){
            emit(Resource.Error(message = "Oops something is not right: $e", data = usersInfo))
        }

        val newUsersInfo = dao.getUsersInfo().map { it.toUserInfo() }
        emit(Resource.Success(newUsersInfo))
    }

}