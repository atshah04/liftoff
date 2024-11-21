package com.example.liftoff.data.classes

import kotlinx.serialization.Serializable

data class User_(val id: Int, val username: String, val password: String)
data class Users(val users_information: List<User_>)

@Serializable
data class User (
    val id: Int,
    val username: String,
    val password: String
)

@Serializable
data class User2(
    val username: String,
    val password: String
)

data class Quote(
    val content: String,
    val author: String,
    val authorSlug: String,
    val length: Int
)

@Serializable
data class Id(
    val id: Int
)