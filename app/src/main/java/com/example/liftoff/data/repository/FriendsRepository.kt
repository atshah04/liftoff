package com.example.liftoff.data.repository

import com.example.liftoff.data.dto.FriendDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendsRepository(private val supabase: SupabaseClient) {

    // Fetch all friends by user ID
    suspend fun getFriendsByUserId(userId: Int): List<FriendDto> = withContext(Dispatchers.IO) {
        val response = supabase.from("friends").select(
            columns = Columns.list("user_id", "friend_id", "friend_username")
        ) {
            filter {
                eq("user_id", userId)
            }
        }

        val friends = response.decodeList<FriendDto>().map { friend ->
            FriendDto(
                userId = friend.userId,
                friendId = friend.friendId,
                friendUsername = friend.friendUsername
            )
        }

        return@withContext friends
    }

    // Add a friend
    suspend fun addFriend(friend: FriendDto) = withContext(Dispatchers.IO) {
        supabase.from("friends")
            .insert(
                FriendDto(
                    userId = friend.userId,
                    friendId = friend.friendId,
                    friendUsername = friend.friendUsername
                )
            )
    }

    // Remove a friend
    suspend fun removeFriend(userId: Int, friendId: Int) = withContext(Dispatchers.IO) {
        supabase.from("friends").delete {
            filter {
                eq("user_id", userId)
                eq("friend_id", friendId)
            }
        }
    }
}
