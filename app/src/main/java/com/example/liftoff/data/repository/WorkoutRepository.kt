package com.example.liftoff.data.repository

import com.example.liftoff.data.database.SupabaseService
import com.example.liftoff.data.dto.WorkoutDto
import com.example.liftoff.data.model.WorkoutType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutRepository {
    private val postgrest: Postgrest = SupabaseService.postgrest

    // Fetch workouts by user ID
    suspend fun getWorkoutsByUserId(userId: Int): List<WorkoutDto> = withContext(Dispatchers.IO) {
        val response = postgrest.from("workouts").select(
            columns = Columns.list("id", "user_id", "workout_name", "duration", "sets", "reps", "weight")
        ) {
            filter {
                eq("user_id", userId)
            }
        }

        val workouts = response.decodeList<Map<String, Any>>().map { row ->
            val workoutType = if (row["duration"] != null) {
                WorkoutType.Timed(row["duration"] as Int)
            } else {
                WorkoutType.Strength(
                    sets = row["sets"] as Int,
                    reps = row["reps"] as Int,
                    weight = row["weight"] as Double
                )
            }

            WorkoutDto(
                id = row["id"] as Int,
                userId = row["user_id"] as Int,
                workoutName = row["workout_name"] as String,
                workoutType = workoutType
            )
        }

        return@withContext workouts
    }


    // Create a new workout
    suspend fun createWorkout(workout: WorkoutDto) = withContext(Dispatchers.IO) {
        val workoutData = when (workout.workoutType) {
            is WorkoutType.Timed -> mapOf(
                "user_id" to workout.userId,
                "workout_name" to workout.workoutName,
                "duration" to (workout.workoutType).duration
            )
            is WorkoutType.Strength -> mapOf(
                "user_id" to workout.userId,
                "workout_name" to workout.workoutName,
                "sets" to (workout.workoutType).sets,
                "reps" to (workout.workoutType).reps,
                "weight" to (workout.workoutType).weight
            )
        }

        postgrest.from("workouts").insert(workoutData)
    }

}
