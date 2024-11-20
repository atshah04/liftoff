package com.example.liftoff.data.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.liftoff.data.dto.FriendDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FriendsViewModel : ViewModel() {
    private val _friends = MutableStateFlow<List<FriendDto>>(listOf())
    val friends: StateFlow<List<FriendDto>> get() = _friends

    private val _filteredFriends = MutableStateFlow<List<FriendDto>>(listOf())
    val filteredFriends: StateFlow<List<FriendDto>> get() = _filteredFriends

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible: StateFlow<Boolean> get() = _isSearchVisible

    private val _search = MutableStateFlow(TextFieldValue(""))
    val search: StateFlow<TextFieldValue> get() = _search

    private val _isAddingFriend = MutableStateFlow(false)
    val isAddingFriend: StateFlow<Boolean> get() = _isAddingFriend

    // Setter functions to modify the state
    fun setFriends(friendsList: List<FriendDto>) {
        _friends.value = friendsList
    }

    fun setFilteredFriends(filteredList: List<FriendDto>) {
        _filteredFriends.value = filteredList
    }

    fun setSearchVisibility(isVisible: Boolean) {
        _isSearchVisible.value = isVisible
    }

    fun setSearchText(searchText: TextFieldValue) {
        _search.value = searchText
    }

    fun setIsAddingFriend(isAdding: Boolean) {
        _isAddingFriend.value = isAdding
    }
}