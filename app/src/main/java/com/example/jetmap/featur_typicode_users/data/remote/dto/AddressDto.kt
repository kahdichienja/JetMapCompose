package com.example.jetmap.featur_typicode_users.data.remote.dto

import com.example.jetmap.featur_typicode_users.domain.model.Address

data class AddressDto(
    val city: String,
    val geo: GeoDto,
    val street: String,
    val suite: String,
    val zipcode: String
){
    fun toAddress(): Address{
        return Address(
            city = city,
            geo = geo.toGeo(),
            street = street,
            zipcode = zipcode,
            suite = suite
        )
    }
}