package com.example.liftoff.data.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GenerateViewModel : ViewModel() {
    val Exercices = listOf("Bicep Exercises", "Chest Exercises", "Legs Exercises", "Back Exercises")
    val exerciseMap = mapOf(
        "chest exercises" to listOf(
            "Push-ups" to 0.6,
            "Bench Press" to 1.0,
            "Incline Bench Press" to 0.8,
            "Chest Fly" to 0.4,
            "Incline Dumbbell Fly" to 0.5,
            "Cable Crossover" to 0.3,
            "Dips" to 0.7,
            "Pec Deck Machine" to 0.4
        ),
        "back exercises" to listOf(
            "Pull-ups" to 0.8,
            "Lat Pulldowns" to 0.7,
            "Deadlifts" to 1.5,
            "Bent-Over Rows" to 0.9,
            "Seated Cable Rows" to 0.7,
            "T-Bar Rows" to 0.8,
            "Single-Arm Dumbbell Rows" to 0.5,
            "Inverted Rows" to 0.4
        ),
        "legs exercises" to listOf(
            "Squats" to 1.2,
            "Lunges" to 0.6,
            "Leg Press" to 1.8,
            "Step-Ups" to 0.5,
            "Hamstring Curls" to 0.4,
            "Calf Raises" to 0.5,
            "Glute Bridges" to 0.7
        ),
        "bicep exercises" to listOf(
            "Bicep Curls" to 0.4,
            "Hammer Curls" to 0.4,
            "Concentration Curls" to 0.3,
            "Preacher Curls" to 0.35,
            "Incline Dumbbell Curls" to 0.4,
            "Cable Bicep Curls" to 0.3,
            "Chin-Ups" to 0.8,
            "Reverse Curls" to 0.35
        )
    )

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
