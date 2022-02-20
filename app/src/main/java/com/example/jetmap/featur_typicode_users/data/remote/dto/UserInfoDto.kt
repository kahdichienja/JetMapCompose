package com.example.jetmap.featur_typicode_users.data.remote.dto

import com.example.jetmap.featur_typicode_users.domain.model.UserInfo

data class UserInfoDto(
    val address: AddressDto,
    val company: CompanyDto,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String
){
    fun toUserInfo(): UserInfo{
        return UserInfo(
            address= address.toAddress(),
            company = company.toCompany(),
            email = email,
            id =id,
            name=name,
            phone = phone,
            username = username,
            website = website
        )
    }
}