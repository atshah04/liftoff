package com.example.liftoff

import com.example.liftoff.data.classes.GlobalState
import com.example.liftoff.data.viewmodel.MainViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MVMTest {
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel()
    }

    @Test
    fun `default GlobalState is initialized correctly`() {
        val defaultState = GlobalState()
        assertEquals(defaultState, viewModel.gs.value)
    }

    @Test
    fun `setGS updates GlobalState flow`() {
        val updatedState = GlobalState(
            userId = 1,
            loggedIn = true,
            username = "Alice",
        )
        viewModel.setGS(updatedState)

        assertEquals(updatedState, viewModel.gs.value)
        assertNotEquals(viewModel.gs.value, GlobalState(false, "Alice", 1))
    }
}
