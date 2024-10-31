package com.example.liftoff.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.liftoff.data.repository.UserRepository
import com.example.liftoff.data.dto.UserDto

class UserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    // Mutable state to store user data
    private val _user = mutableStateOf<UserDto?>(null)
    val user: State<UserDto?> = _user

    // Fetch user data from the repository
    fun getUserById(id: Int) {
        viewModelScope.launch {
            val userData = userRepository.getUserById(id)
            _user.value = userData
        }
    }
}