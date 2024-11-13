package com.example.liftoff.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendDto(
    @SerialName("user_id") val userId: Int,
    @SerialName("friend_id") val friendId: Int,
    @SerialName("friend_username") val friendUsername: String
)
