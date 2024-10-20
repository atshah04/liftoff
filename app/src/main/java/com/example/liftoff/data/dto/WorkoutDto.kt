package com.example.liftoff.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkoutDto(
    val id: Int?= null,
    @SerialName("user_id") val userId: Int,
    val name: String,
    val date: String,
    val exercises: List<ExerciseDto>
)
