package com.example.liftoff.data.repository

import com.example.liftoff.data.database.SupabaseService
import com.example.liftoff.data.dto.UserDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    private val postgrest: Postgrest = SupabaseService.postgrest

    // Fetch a user by their ID
    suspend fun getUserById(id: Int): UserDto? = withContext(Dispatchers.IO) {
        val response = postgrest.from("users")
            .select(columns = Columns.list("id", "username", "password")) {
                filter {
                    eq("id", id)
                }
            }

        val users = response.decodeList<UserDto>()
        return@withContext users.firstOrNull()
    }

    // Create a new user
    suspend fun createUser(user: UserDto) = withContext(Dispatchers.IO) {
        postgrest.from("users")
            .insert(mapOf("username" to user.username, "password" to user.password))
    }
}
