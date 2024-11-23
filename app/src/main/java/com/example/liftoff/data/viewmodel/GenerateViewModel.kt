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

    private val _weight = MutableStateFlow(TextFieldValue(""))
    val weight: StateFlow<TextFieldValue> get() = _weight

    // StateFlow for checked states of exercises
    private val _checkedStates = MutableStateFlow(List(Exercices.size) { false })
    val checkedStates: StateFlow<List<Boolean>> get() = _checkedStates

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> get() = _error

    private val _override = MutableStateFlow(false)
    val override: StateFlow<Boolean> get() = _override

    // Setter functions
    fun setMinutesInput(input: TextFieldValue) {
        _minutesInput.value = input
    }

    fun setCheckedState(index: Int, state: Boolean) {
        val updatedStates = _checkedStates.value.toMutableList()
        updatedStates[index] = state
        _checkedStates.value = updatedStates
    }

    fun setWeightInput(input: TextFieldValue) {
        _weight.value = input
    }

    fun setError(error: Boolean) { _error.value = error }

    fun setOverride(error: Boolean) { _override.value = error }
}
