package com.example.liftoff.ui.newacc

//import com.example.liftoff.data.database.SupabaseService.SUPABASE_KEY
//import com.example.liftoff.data.database.SupabaseService.SUPABASE_URL
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
import com.example.liftoff.data.viewmodel.LoginViewModel
import com.example.liftoff.data.viewmodel.MainViewModel
import com.example.liftoff.data.viewmodel.NewAccViewModel

val supabase =
    SupabaseService.client

@Composable
fun NewAccScreen(navFuncs: Map<String, () -> Unit>, mvm: MainViewModel, navm: LoginViewModel) {
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
            CreatePage(navFuncs, mvm, navm)
        }
    }
}

@Composable
fun CreatePage(navFuncs: Map<String, () -> Unit>, mvm: MainViewModel, navm: LoginViewModel) {
    // Data that will be displayed on the cards
    val data = remember { mutableStateOf<List<Users>>(emptyList()) }
    val setUserid = { uid: Int -> navm.setUserId(uid)}
    val userid by navm.userId.collectAsState()
    val setUsername = { username: TextFieldValue -> navm.setUser(username) }
    val username by navm.username.collectAsState()
    val setPw = { pass: TextFieldValue -> navm.setPass(pass) }
    val setLoggedIn = { isLoggedIn: Boolean -> navm.setLoggedIn(isLoggedIn) }
    val setLogInFail = { logInFail: Boolean -> navm.setLogInFail(logInFail) }
    val setAccountFail = { accFail: Boolean -> navm.setAccountFail(accFail) }
    val password by navm.password.collectAsState()
    val loggedIn by navm.loggedIn.collectAsState()
    val logInFail by navm.logInFail.collectAsState()
    val accountFail by navm.accountFail.collectAsState()

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
                setLoggedIn(false)
                setUsername(TextFieldValue(""))
                setPw(TextFieldValue(""))
                mvm.setGS(GlobalState(true, username.text, userid))
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


