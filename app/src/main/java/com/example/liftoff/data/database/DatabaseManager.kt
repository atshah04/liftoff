package com.example.liftoff.data.database

import com.example.liftoff.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

object SupabaseService {
    private const val SUPABASE_URL = BuildConfig.SUPABASE_URL
    private const val SUPABASE_KEY = BuildConfig.SUPABASE_KEY

    // Initialize the Supabase client
    private val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                scheme = "app"
                host = "supabase.com"
            }
            install(Storage)
        }
    }

    // Provide the Postgrest client (used for database queries)
    val postgrest: Postgrest
        get() = client.postgrest

    // Provide the Auth client (used for authentication)
    val auth: Auth
        get() = client.auth

    // Provide the Storage client (used for storage operations)
    val storage: Storage
        get() = client.storage
}
