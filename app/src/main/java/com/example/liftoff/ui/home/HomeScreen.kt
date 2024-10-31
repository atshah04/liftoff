package com.example.liftoff.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liftoff.data.database.SupabaseService
//import com.example.liftoff.data.database.SupabaseService.SUPABASE_KEY
//import com.example.liftoff.data.database.SupabaseService.SUPABASE_URL
import com.example.liftoff.data.classes.*
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.liftoff.ui.layout.*

val supabase =
    SupabaseService.client

val default_mod = Modifier
    .padding(16.dp)

@Composable
fun HomeScreen(gs: GlobalState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column (
            modifier = Modifier.fillMaxSize()
        )
        {
            Text(
                text = "Hi, ${gs.username}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(top = 16.dp)
            )
            User_Information()
        }
    }
}

@Composable
fun User_Information() {
    // can choose to use or not use remember when defining the mutablestate of lists
    val notes = remember { mutableStateListOf<User>() }

    // Data that will be displayed on the cards
    var data = remember { mutableStateOf<List<Users>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val id = 6
            val results = supabase.from("users")
                .select(columns = Columns.list("id", "username", "password")) {
                    filter {
                        eq("id", id)
                    }
                }
            val users = results.decodeList<User>()
            notes.addAll(users)

            data.value = listOf(
                Users(users_information = users.map { user ->
                    User_(id = user.id, username = user.username, password = user.password)
                }))

        }
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.TopStart) {
        Text(
            text = "User Information",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(top = 16.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(top = 80.dp)
        ) {
            UserColumn(items = data.value)
        }
    }
}

@Composable
fun Card(session: Users) {

    Column(modifier = default_mod) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "ID", modifier = Modifier.weight(1f))
            Text(text = "username", modifier = Modifier.weight(0.5f))
            Text(text = "password", modifier = Modifier.weight(0.5f))
        }
        session.users_information.forEach { info ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = info.id.toString(), modifier = Modifier.weight(1f))
                Text(text = info.username, modifier = Modifier.weight(0.5f))
                Text(text = info.password, modifier = Modifier.weight(0.5f))
            }
        }

    }
}
