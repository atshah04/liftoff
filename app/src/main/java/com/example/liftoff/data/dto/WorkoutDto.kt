package com.example.liftoff.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkoutDto(
    @SerialName("user_id") val userId: Int,
    @SerialName("workout_type") val workoutType: String,
    @SerialName("workout_name") val workoutName: String,
    val duration: Int? = null,
    val sets: Int? = null,
    val reps: Int? = null,
    val weight: Double? = null
)
