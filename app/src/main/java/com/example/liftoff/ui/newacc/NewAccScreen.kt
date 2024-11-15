package com.example.liftoff.ui.newacc

//import com.example.liftoff.data.database.SupabaseService.SUPABASE_KEY
//import com.example.liftoff.data.database.SupabaseService.SUPABASE_URL
import android.provider.Settings.Global
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.liftoff.data.database.SupabaseService
import com.example.liftoff.ui.components.*
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import com.example.liftoff.data.classes.*

val supabase =
    SupabaseService.client

@Composable
fun NewAccScreen(navFuncs: Map<String, () -> Unit>, login: (GlobalState) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            TextL("New")
            TextL("Account")
            CreatePage(navFuncs, login)
        }
    }
}

@Composable
fun CreatePage(navFuncs: Map<String, () -> Unit>, setGlobals: (GlobalState) -> Unit) {
    // Data that will be displayed on the cards
    var data = remember { mutableStateOf<List<Users>>(emptyList()) }
    var (userid, setUserid) = remember { mutableStateOf(-1) }
    var (username, setUsername) = remember { mutableStateOf(TextFieldValue("")) }
    var (password, setPw) = remember { mutableStateOf(TextFieldValue("")) }
    var (loggedIn, setLoggedIn) = remember { mutableStateOf(false) }
    var (accountFail, setAccountFail) = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.TopStart) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
        ) {
            TextH("Username")
            DefaultTextField(username, setUsername)
            TextH("Password")
            PassTextField(password, setPw)
            Row (
                Modifier.width(250.dp)
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    navFuncs["options"]!!.invoke()
                }) {
                    Text("Back")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            val results = supabase.from("users")
                                .select(columns = Columns.list("id", "username", "password")) {
                                    filter {
                                        eq("username", username.text)
                                    }
                                }
                            val users = results.decodeList<User>()
                            if (users.isEmpty() && username.text != "" && password.text != "") {
                                supabase.from("users")
                                    .insert(User2(username.text, password.text))
                                setLoggedIn(true)
                                val results = supabase.from("users")
                                    .select(columns = Columns.list("id", "username", "password")) {
                                        filter {
                                            eq("username", username.text)
                                        }
                                    }
                                val users = results.decodeList<User>()
                                setUserid(users[0].id)
                            } else {
                                setAccountFail(true)
                            }
                        }
                    }
                }) {
                    Text("Register Account")
                }
            }
            if (loggedIn) {
                navFuncs["home"]!!.invoke()
                setGlobals(GlobalState(true, username.text, userid))
            }
            if (accountFail) Modal(
                { setAccountFail(false)},
                { setAccountFail(false)},
                "Account Creation Failed",
                "User with username already exists, please try again.",
                Icons.Default.Info)
        }
    }
}


