package com.example.liftoff.ui.home

import com.example.liftoff.ui.notifications.*
import androidx.compose.material.icons.Icons
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
import com.example.liftoff.ui.notifications.NotificationHandler
import java.lang.Thread.sleep
import android.app.TimePickerDialog
import android.content.ContentUris
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.activity.compose.setContent
import android.os.Bundle
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle


suspend fun motivationalquotes(): Quote? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.realinspire.tech/v1/quotes/random?minLength=75&maxLength=125")
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
    var data = remember { mutableStateOf<List<Int>>(emptyList()) }
    var queried = remember { mutableStateOf(false) }
    val gs by mvm.gs.collectAsState()
    val context = LocalContext.current
    val notificationHandler = NotificationHandler(context)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {quote = motivationalquotes()}
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize().weight(0.70f)
        ) {
            Image (
                painter = painterResource(R.drawable.fitnessbackground),
                contentDescription = "top background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.graphicsLayer(alpha = 0.15f).fillMaxHeight()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Card (
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .padding(8.dp)
                        .clickable{
                            mvm.setGS(GlobalState(false, "", -1))
                            data.value = emptyList()
                            queried.value = false
                            navFuncs["login"]!!.invoke()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).size(48.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Logout", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
                NotificationScheduler(notificationHandler = notificationHandler, context = context)
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 100.dp)) {
                User_Information(gs, data, queried)

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 500.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                quote?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(20.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("\"")
                                withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 20.sp)) {
                                    append(it.content)
                                }
                                append("\"\n\n")
                                withStyle(style = SpanStyle(color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
                                    append(it.author)
                                }
                            },
                            textAlign = TextAlign.Left,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationScheduler(notificationHandler: NotificationHandler, context: Context) {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        hour = selectedHour
                        minute = selectedMinute
                        notificationHandler.scheduleWorkoutNotification(context, hour, minute)
                    },
                    hour,
                    minute,
                    true
                )
                timePickerDialog.show()
            }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                fontWeight = FontWeight.Bold,
                text = "Select Workout Time",
            )

            IconButton(onClick = {
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        hour = selectedHour
                        minute = selectedMinute
                        notificationHandler.scheduleWorkoutNotification(context, hour, minute)
                    },
                    hour,
                    minute,
                    true
                )
                timePickerDialog.show()

            }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Add Workout",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun User_Information(gs: GlobalState, data: MutableState<List<Int>>, queried: MutableState<Boolean>) {
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
        .padding(10.dp),
        contentAlignment = Alignment.TopStart) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Hi ${gs.username},",
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            )
            if (data.value?.isNotEmpty() == true && queried.value) {
                Text(
                    text = "You're on a hot streak of ${data.value?.size} this week!",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.fire_emoji),
                    contentDescription = "Liftoff Logo",
                    modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                )
            }
            else if (queried.value) {
                Text(
                    text = "Let's get you started with a workout!",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.rocket_emoji),
                    contentDescription = "Liftoff Logo",
                    modifier = Modifier.size(200.dp),
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
