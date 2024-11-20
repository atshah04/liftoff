package com.example.liftoff.data.classes

import androidx.compose.ui.text.input.*

data class GlobalState (
    val loggedIn: Boolean = false,
    val username: String = "",
    val userId: Int = -1
)
