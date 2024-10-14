package com.example.liftoff.data.model

sealed class WorkoutType {
    data class Timed(val duration: Int) : WorkoutType()
    data class Strength(val sets: Int, val reps: Int, val weight: Double) : WorkoutType()
}
