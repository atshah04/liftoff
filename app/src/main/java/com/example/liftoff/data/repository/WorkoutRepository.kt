package com.example.liftoff.data.repository

import com.example.liftoff.data.dto.WorkoutDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutRepository(private val supabase: SupabaseClient) {

    // Fetch workouts by user ID
    suspend fun getWorkoutsByUserId(userId: Int): List<WorkoutDto> = withContext(Dispatchers.IO) {
        val response = supabase.from("workouts").select(
            columns = Columns.list("id", "user_id", "name", "date", "exercises")
        ) {
            filter {
                eq("user_id", userId)
            }
        }

        val workouts = response.decodeList<WorkoutDto>().map { workout ->
            WorkoutDto(
                userId = workout.userId,
                name = workout.name,
                date = workout.date,
                exercises = workout.exercises
            )
        }

        return@withContext workouts
    }

    // Create a new workout
    suspend fun createWorkout(workoutDto: WorkoutDto) {
        val workoutData = WorkoutDto(
            userId = workoutDto.userId,
            name = workoutDto.name,
            date = workoutDto.date,
            exercises = workoutDto.exercises
        )

        supabase.from("workouts").insert(workoutData)
    }
}
