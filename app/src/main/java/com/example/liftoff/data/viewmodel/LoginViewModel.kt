package com.example.liftoff.data.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.liftoff.data.classes.Users
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {
    private val _data = MutableStateFlow<List<Users>>(emptyList())
    val data: StateFlow<List<Users>> = _data

    private val _userId = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId

    private val _username = MutableStateFlow(TextFieldValue(""))
    val username: StateFlow<TextFieldValue> get() = _username

    private val _password = MutableStateFlow(TextFieldValue(""))
    val password: StateFlow<TextFieldValue> get() = _password

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn: StateFlow<Boolean> get() = _loggedIn

    private val _logInFail = MutableStateFlow(false)
    val logInFail: StateFlow<Boolean> get() = _logInFail

    private val _accountFail = MutableStateFlow(false)
    val accountFail: StateFlow<Boolean> get() = _accountFail

    fun setData(data: List<Users>) {
        _data.value = data
    }
    fun setUser(user: TextFieldValue) {
        _username.value = user
    }
    fun setPass(pass: TextFieldValue) {
        _password.value = pass
    }

    fun setUserId(userid: Int) {
        _userId.value = userid
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        _loggedIn.value = isLoggedIn
    }

    fun setLogInFail(logInFail: Boolean) {
        _logInFail.value = logInFail
    }

    fun setAccountFail(accFail: Boolean) {
        _accountFail.value = accFail
    }
}