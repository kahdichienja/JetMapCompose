package com.example.jetmap.featur_typicode_users.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jetmap.featur_typicode_users.domain.model.Address
import com.example.jetmap.featur_typicode_users.domain.model.Company
import com.example.jetmap.featur_typicode_users.domain.model.UserInfo

@Entity
data class UserInfoEntity(
    val email: String,
    @PrimaryKey val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String,
//    val address: Address,
//    val company: Company,
){
    fun toUserInfo(): UserInfo{
        return UserInfo(
            id = id,
            name = name,
            email = email,
            username = username,
            website = website,
            phone = phone,
//            company = company,
//            address = address,
        )
    }
}
