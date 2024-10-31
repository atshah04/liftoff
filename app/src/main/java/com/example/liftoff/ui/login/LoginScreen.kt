package com.example.liftoff.ui.login

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
import kotlinx.serialization.Serializable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import com.example.liftoff.ui.navigation.GlobalState

data class User_(val id: Int, val username: String, val password: String)
data class Users(val users_information: List<User_>)

val supabase =
    SupabaseService.client

@Composable
fun LoginScreen(home: () -> Unit, login: (GlobalState) -> Unit) {
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
            TextL("LiftOff")
            LoginPage(home, login)
        }
    }
}
@Serializable
data class User (
    val id: Int,
    val username: String,
    val password: String
)

@Serializable
data class User2 (
    val username: String,
    val password: String
)

@Composable
fun LoginPage(home: () -> Unit, setGlobals: (GlobalState) -> Unit) {
    // Data that will be displayed on the cards
    var data = remember { mutableStateOf<List<Users>>(emptyList()) }
    var (username, setUsername) = remember { mutableStateOf(TextFieldValue("")) }
    var (password, setPw) = remember { mutableStateOf(TextFieldValue("")) }
    var (loggedIn, setLoggedIn) = remember { mutableStateOf(false) }
    var (logInFail, setLogInFail) = remember { mutableStateOf(false) }
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
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            val results = supabase.from("users")
                                .select(columns = Columns.list("id", "username", "password")) {
                                    filter {
                                        eq("username", username.text)
                                        eq("password", password.text)
                                    }
                                }
                            val users = results.decodeList<User>()
                            if (users.isEmpty()) setLogInFail(true)
                            else {
                                data.value = listOf(
                                    Users(users_information = users.map { user ->
                                        User_(
                                            id = user.id,
                                            username = user.username,
                                            password = user.password
                                        )
                                    })
                                )
                                setLoggedIn(true)
                            }
                        }
                    }
                }) {
                    Text("Log In")
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
                            if (users.isEmpty()) {
                                supabase.from("users")
                                    .insert(User2(username.text, password.text))
                                setLoggedIn(true)
                            } else {
                                setAccountFail(true)
                            }
                        }
                    }
                }) {
                    Text("Create Account")
                }
            }
            if (loggedIn) {
                home()
                setGlobals(GlobalState(true, username.text))
            }
            if (accountFail) Modal(
                { setAccountFail(false)},
                { setAccountFail(false)},
                "Account Creation Failed",
                "User with username already exists, please try again.",
                Icons.Default.Info)
            if (logInFail) Modal(
                { setLogInFail(false)},
                { setLogInFail(false)},
                "Login Failed",
                "Invalid credentials please try again.",
                Icons.Default.Info)
//            DynamicColumn(items = data.value)
        }
    }
}


