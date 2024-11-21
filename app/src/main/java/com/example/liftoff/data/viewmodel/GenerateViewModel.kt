package com.example.liftoff.data.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GenerateViewModel : ViewModel() {
    val Exercices = listOf("Bicep Exercises", "Chest Exercises", "Legs Exercises", "Back Exercises")

    // StateFlow for minutes input
    private val _minutesInput = MutableStateFlow(TextFieldValue(""))
    val minutesInput: StateFlow<TextFieldValue> get() = _minutesInput

    // StateFlow for checked states of exercises
    private val _checkedStates = MutableStateFlow(List(Exercices.size) { false })
    val checkedStates: StateFlow<List<Boolean>> get() = _checkedStates

    // Setter functions
    fun setMinutesInput(input: TextFieldValue) {
        _minutesInput.value = input
    }

    fun setCheckedState(index: Int, state: Boolean) {
        val updatedStates = _checkedStates.value.toMutableList()
        updatedStates[index] = state
        _checkedStates.value = updatedStates
    }
}
