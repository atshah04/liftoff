package com.example.liftoff.data.dto

import com.example.liftoff.data.model.WorkoutType

data class WorkoutDto(
    val id: Int,
    val userId: Int,
    val workoutName: String,
    val workoutType: WorkoutType
)
