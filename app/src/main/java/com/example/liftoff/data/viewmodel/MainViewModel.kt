package com.example.liftoff.data.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.liftoff.data.classes.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _gs = MutableStateFlow(GlobalState())
    val gs: StateFlow<GlobalState> get() = _gs

    fun setGS(gs: GlobalState) {
        _gs.value = gs
    }
}