package com.example.liftoff.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


suspend fun motivationalquotes(): Quote? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.realinspire.tech/v1/quotes/random")
        .build()
    return client.newCall(request).execute().use { response ->
        val resp = response.body!!.string()
        val gson = Gson()
        val quoteListType = object : TypeToken<List<Quote>>() {}.type
        val quotes: List<Quote> = gson.fromJson(resp, quoteListType)
        quotes.first()
    }
    }

val supabase =
    SupabaseService.client

val default_mod = Modifier
    .padding(16.dp)

@Composable
fun HomeScreen(navFuncs: Map<String, () -> Unit>, gs: GlobalState, setGS: (GlobalState) -> Unit) {
    var quote by remember { mutableStateOf<Quote?>(null) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {quote = motivationalquotes()}

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )

        {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Hi, ${gs.username}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Button(onClick = {
                    setGS(GlobalState(false, "", -1))
                    navFuncs["login"]!!.invoke()
                }, Modifier.padding(top = 16.dp)) {
                    Text("Log Out")
                }
            }
            quote?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "\"${it.content}\" - ${it.author}",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

            }
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

    val notes = remember { mutableStateListOf<User>() }


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
