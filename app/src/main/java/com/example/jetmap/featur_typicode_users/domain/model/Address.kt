package com.example.jetmap.featur_typicode_users.domain.model
data class Address(
    val city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)
