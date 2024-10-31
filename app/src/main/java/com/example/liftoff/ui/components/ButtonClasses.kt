package com.example.liftoff.ui.components

import androidx.compose.material3.Text
import com.example.liftoff.data.classes.*
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.liftoff.data.database.SupabaseService
import kotlinx.coroutines.coroutineScope
import androidx.compose.material3.Button
import androidx.compose.runtime.*

val supabase =
    SupabaseService.client

@Composable
fun DBButton() {

}