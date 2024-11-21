package com.example.liftoff.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.example.liftoff.data.viewmodel.MainViewModel
import com.example.liftoff.ui.components.*
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
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.liftoff.R
import java.lang.Thread.sleep


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
fun HomeScreen(navFuncs: Map<String, () -> Unit>, mvm: MainViewModel) {
    var quote by remember { mutableStateOf<Quote?>(null) }
    val gs by mvm.gs.collectAsState()

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
            horizontalAlignment = Alignment.End
        )

        {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    mvm.setGS(GlobalState(false, "", -1))
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
            User_Information(gs)
        }
    }
}

@Composable
fun User_Information(gs: GlobalState) {

    val notes = remember { mutableStateListOf<User>() }
    var data = remember { mutableStateOf<List<Int>>(emptyList()) }
    var queried = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val week_back = Calendar.getInstance()
            week_back.add(Calendar.HOUR_OF_DAY, -7*24)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(week_back.time)
            val results = supabase.from("workouts")
                .select(columns = Columns.list("id")) {
                    filter {
                        eq("user_id", gs.userId)
                        gt("date", date)
                    }
                }
            val ids = results.decodeList<Id>()

            data.value = ids.map { id -> id.id }
            queried.value = true
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.TopStart) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(top = 80.dp)
        ) {
            Text(
                text = "Hi, ${gs.username}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(top = 16.dp)
            )
            if (data.value?.isNotEmpty() == true && queried.value) {
                TextS("You're on a hot streak this week!")
                Image(
                    painter = painterResource(id = R.drawable.fire_emoji),
                    contentDescription = "Liftoff Logo",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            else if (queried.value) {
                TextS("Let's get you started with a workout!")
                Image(
                    painter = painterResource(id = R.drawable.rocket_emoji),
                    contentDescription = "Liftoff Logo",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
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
