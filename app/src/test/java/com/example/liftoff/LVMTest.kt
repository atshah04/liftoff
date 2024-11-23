package com.example.liftoff

import androidx.compose.ui.text.input.TextFieldValue
import com.example.liftoff.data.classes.User_
import com.example.liftoff.data.classes.Users
import com.example.liftoff.data.viewmodel.LoginViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LVMTest {
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel()
    }

    @Test
    fun `setData updates data flow`() {
        val userList = listOf(Users(listOf(
            User_(id = 1, username = "Alice", password = "pass1"),
            User_(id = 2, username = "Bob", password = "pass2")
        )))
        viewModel.setData(userList)

        assertEquals(userList, viewModel.data.value)
    }

    @Test
    fun `setUser updates username flow`() {
        val username = TextFieldValue("Alice")
        viewModel.setUser(username)

        assertEquals(username, viewModel.username.value)
    }

    @Test
    fun `setPass updates password flow`() {
        val password = TextFieldValue("securepass")
        viewModel.setPass(password)

        assertEquals(password, viewModel.password.value)
    }

    @Test
    fun `setUserId updates userId flow`() {
        val userId = 42
        viewModel.setUserId(userId)

        assertEquals(userId, viewModel.userId.value)
    }

    @Test
    fun `setLoggedIn updates loggedIn flow`() {
        viewModel.setLoggedIn(true)
        assertTrue(viewModel.loggedIn.value)

        viewModel.setLoggedIn(false)
        assertFalse(viewModel.loggedIn.value)
    }

    @Test
    fun `setLogInFail updates logInFail flow`() {
        viewModel.setLogInFail(true)
        assertTrue(viewModel.logInFail.value)

        viewModel.setLogInFail(false)
        assertFalse(viewModel.logInFail.value)
    }

    @Test
    fun `setAccountFail updates accountFail flow`() {
        viewModel.setAccountFail(true)
        assertTrue(viewModel.accountFail.value)

        viewModel.setAccountFail(false)
        assertFalse(viewModel.accountFail.value)
    }

    @Test
    fun `default flows are initialized correctly`() {
        assertTrue(viewModel.data.value.isEmpty())
        assertEquals(-1, viewModel.userId.value)
        assertEquals(TextFieldValue(""), viewModel.username.value)
        assertEquals(TextFieldValue(""), viewModel.password.value)
        assertFalse(viewModel.loggedIn.value)
        assertFalse(viewModel.logInFail.value)
        assertFalse(viewModel.accountFail.value)
    }
}
