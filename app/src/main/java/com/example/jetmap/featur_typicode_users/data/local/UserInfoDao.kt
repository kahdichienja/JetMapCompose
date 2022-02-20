package com.example.jetmap.featur_typicode_users.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetmap.featur_typicode_users.data.local.entity.UserInfoEntity

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: List<UserInfoEntity>)

    @Query("DELETE FROM userinfoentity WHERE username IN(:username)")
    suspend fun deleteUserInf(username: List<String>)

//    @Query("SELECT * FROM userinfoentity WHERE username LIKE '%' || :username || '%'")
    @Query("SELECT * FROM userinfoentity")
    suspend fun getUsersInfo( ): List<UserInfoEntity>
}