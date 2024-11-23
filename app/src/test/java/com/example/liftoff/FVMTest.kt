package com.example.liftoff

import androidx.compose.ui.text.input.TextFieldValue
import com.example.liftoff.data.dto.FriendDto
import com.example.liftoff.data.viewmodel.FriendsViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FVMTest {
    private lateinit var viewModel: FriendsViewModel

    @Before
    fun setup() {
        viewModel = FriendsViewModel()
    }

    @Test
    fun `setFriends updates friends flow`() {
        val friendList = listOf(
            FriendDto(userId = 1, friendId = 101, friendUsername = "Alice"),
            FriendDto(userId = 2, friendId = 102, friendUsername = "Bob")
        )
        viewModel.setFriends(friendList)

        assertEquals(friendList, viewModel.friends.value)
    }

    @Test
    fun `setFilteredFriends updates filteredFriends flow`() {
        val filteredList = listOf(
            FriendDto(userId = 1, friendId = 101, friendUsername = "Alice")
        )
        viewModel.setFilteredFriends(filteredList)

        assertEquals(filteredList, viewModel.filteredFriends.value)
    }

    @Test
    fun `setSearchVisibility updates isSearchVisible flow`() {
        viewModel.setSearchVisibility(true)
        assertTrue(viewModel.isSearchVisible.value)

        viewModel.setSearchVisibility(false)
        assertFalse(viewModel.isSearchVisible.value)
    }

    @Test
    fun `setSearchText updates search flow`() {
        val searchText = TextFieldValue("Alice")
        viewModel.setSearchText(searchText)

        assertEquals(searchText, viewModel.search.value)
    }

    @Test
    fun `setIsAddingFriend updates isAddingFriend flow`() {
        viewModel.setIsAddingFriend(true)
        assertTrue(viewModel.isAddingFriend.value)

        viewModel.setIsAddingFriend(false)
        assertFalse(viewModel.isAddingFriend.value)
    }

    @Test
    fun `default flows are initialized correctly`() {
        assertTrue(viewModel.friends.value.isEmpty())
        assertTrue(viewModel.filteredFriends.value.isEmpty())
        assertFalse(viewModel.isSearchVisible.value)
        assertEquals(TextFieldValue(""), viewModel.search.value)
        assertFalse(viewModel.isAddingFriend.value)
    }
}