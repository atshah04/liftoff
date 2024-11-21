package com.example.liftoff.ui.login

//import com.example.liftoff.data.database.SupabaseService.SUPABASE_KEY
//import com.example.liftoff.data.database.SupabaseService.SUPABASE_URL
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import com.example.liftoff.R
import com.example.liftoff.data.classes.*
import com.example.liftoff.data.viewmodel.LoginViewModel
import com.example.liftoff.data.viewmodel.MainViewModel
import org.w3c.dom.Text

val supabase =
    SupabaseService.client

@Composable
fun LoginScreen(navFuncs: Map<String, ()->Unit>, mvm: MainViewModel, lgvm: LoginViewModel) {
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
            LoginPage(navFuncs, mvm, lgvm)
        }
    }
}

@Composable
fun LoginPage(navFuncs: Map<String, ()->Unit>, mvm: MainViewModel, lgvm: LoginViewModel) {
    val data = remember { mutableStateOf<List<Users>>(emptyList()) }
    val setUserid = { uid: Int -> lgvm.setUserId(uid)}
    val userid by lgvm.userId.collectAsState()
    val setUsername = { username: TextFieldValue -> lgvm.setUser(username) }
    val username by lgvm.username.collectAsState()
    val setPw = { pass: TextFieldValue -> lgvm.setPass(pass) }
    val setLoggedIn = { isLoggedIn: Boolean -> lgvm.setLoggedIn(isLoggedIn) }
    val setLogInFail = { logInFail: Boolean -> lgvm.setLogInFail(logInFail) }
    val setAccountFail = { accFail: Boolean -> lgvm.setAccountFail(accFail) }
    val password by lgvm.password.collectAsState()
    val loggedIn by lgvm.loggedIn.collectAsState()
    val logInFail by lgvm.logInFail.collectAsState()
    val accountFail by lgvm.accountFail.collectAsState()

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
            Image(
                painter = painterResource(id = R.drawable.logo_final1),
                contentDescription = "Liftoff Logo",
                modifier = Modifier.fillMaxWidth(),
            )
            DefaultTextField(username, setUsername, "Username")
            PassTextField(password, setPw, "Password")
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
                                setUserid(data.value[0].users_information[0].id)
                            }
                        }
                    }
                }) {
                    Text("Log In")
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
            if (logInFail) Modal(
                { setLogInFail(false)},
                { setLogInFail(false)},
                "Login Failed",
                "Invalid credentials please try again.",
                Icons.Default.Info)
        }
    }
}


