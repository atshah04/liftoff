package com.example.liftoff.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val name: String,
    val type: String,
    val duration: Int? = null,
    val sets: Int? = null,
    val reps: Int? = null,
    val weight: Double? = null
)
