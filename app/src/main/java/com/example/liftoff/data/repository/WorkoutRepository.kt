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
            columns = Columns.list("user_id", "workout_type", "workout_name", "duration", "sets", "reps", "weight")
        ) {
            filter {
                eq("user_id", userId)
            }
        }

        val workouts = response.decodeList<WorkoutDto>().map { row ->
            WorkoutDto(
                userId = row.userId,
                workoutType = row.workoutType,
                workoutName = row.workoutName,
                duration = row.duration,
                sets = row.sets,
                reps = row.reps,
                weight = row.weight,
            )
        }

        return@withContext workouts
    }

    // Create a new workout
    suspend fun createWorkout(workoutDto: WorkoutDto) {
        val workoutData = WorkoutDto(
            userId = workoutDto.userId,
            workoutType = workoutDto.workoutType,
            workoutName = workoutDto.workoutName,
            duration = workoutDto.duration,
            sets = workoutDto.sets,
            reps = workoutDto.reps,
            weight = workoutDto.weight
        )

        supabase.from("workouts").insert(workoutData)
    }
}
