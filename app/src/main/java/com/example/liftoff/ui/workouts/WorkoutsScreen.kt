package com.example.liftoff.ui.workouts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
data class Workout(val name: String, val reps: Int, val sets: Int)
data class WorkoutSession(val exercises: List<Workout>)

val default_mod = Modifier
    .clip(RoundedCornerShape(2.dp))
    .border(1.dp, Color.Black)
    .padding(8.dp)
    .fillMaxWidth(0.9f)

@Composable
fun WorkoutsScreen() {

    var data: List<WorkoutSession> = listOf(
        WorkoutSession(exercises = listOf(
            Workout("Push Ups", 15, 3),
            Workout("Pull Ups", 10, 4),
            Workout("Squats", 20, 4),
            Workout("Bench Press", 12, 3),
            Workout("Deadlift", 8, 3))),
        WorkoutSession(exercises = listOf(
            Workout(name = "Bicep Curls", reps = 12, sets = 3),
            Workout(name = "Tricep Dips", reps = 15, sets = 3),
            Workout(name = "Lunges", reps = 20, sets = 4),
            Workout(name = "Plank", reps = 1, sets = 3),
            Workout(name = "Burpees", reps = 15, sets = 3))),
        WorkoutSession(exercises = listOf(
            Workout("Push Ups", 12, 2),
            Workout("Pull Ups", 8, 3),
            Workout("Squats", 6, 2),
            Workout("Bench Press", 4, 1),
            Workout("Deadlift", 4, 2))),
        WorkoutSession(exercises = listOf(
            Workout(name = "Bicep Curls", reps = 12, sets = 2),
            Workout(name = "Tricep Dips", reps = 8, sets = 2),
            Workout(name = "Lunges", reps = 20, sets = 4),
            Workout(name = "Plank", reps = 1, sets = 3),
            Workout(name = "Burpees", reps = 15, sets = 3))),
    )

    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(modifier = Modifier.padding(bottom = 16.dp), text = "Workouts Screen")
        DynamicColumn(items = data)
    }
}

@Composable
fun Card(session: WorkoutSession) {

    Column(modifier = default_mod) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Exercise", modifier = Modifier.weight(1f))
            Text(text = "Reps", modifier = Modifier.weight(0.5f))
            Text(text = "Sets", modifier = Modifier.weight(0.5f))
        }
        session.exercises.forEach { workout ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = workout.name, modifier = Modifier.weight(1f))
                Text(text = workout.reps.toString(), modifier = Modifier.weight(0.5f))
                Text(text = workout.sets.toString(), modifier = Modifier.weight(0.5f))
            }
        }

    }
}

@Composable
fun DynamicColumn(items: List<WorkoutSession>) {
    Column {
        items.forEach { session ->
                Card(session)
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}
