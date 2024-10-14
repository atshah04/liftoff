package com.example.liftoff.data.repository

import com.example.liftoff.data.database.SupabaseService
import com.example.liftoff.data.dto.FriendDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendsRepository {
    private val postgrest: Postgrest = SupabaseService.postgrest

    // Fetch all friends by user ID
    suspend fun getFriendsByUserId(userId: Int): List<String> = withContext(Dispatchers.IO) {
        val response = postgrest.from("friends").select(
            columns = Columns.list("friend_id")
        ) {
            filter {
                eq("user_id", userId)
            }
        }

        val friends = response.decodeList<Map<String, Any>>().map { it["friend_id"] as String }
        return@withContext friends
    }

    // Add a friend
    suspend fun addFriend(friend: FriendDto) = withContext(Dispatchers.IO) {
        postgrest.from("friends")
            .insert(
                mapOf(
                    "user_id" to friend.userId,
                    "friend_id" to friend.friendId
                )
            )
    }

    // Remove a friend
    suspend fun removeFriend(userId: Int, friendId: Int) = withContext(Dispatchers.IO) {
        postgrest.from("friends").delete {
            filter {
                eq("user_id", userId)
                eq("friend_id", friendId)
            }
        }
    }
}
