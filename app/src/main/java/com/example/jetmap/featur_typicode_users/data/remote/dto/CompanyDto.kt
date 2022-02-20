package com.example.jetmap.featur_typicode_users.data.remote.dto

import com.example.jetmap.featur_typicode_users.domain.model.Company

data class CompanyDto(
    val bs: String,
    val catchPhrase: String,
    val name: String
){
    fun toCompany(): Company{
        return Company(
            bs=bs,
            name = name
        )
    }
}