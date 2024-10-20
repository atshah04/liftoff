package com.example.liftoff.ui.workouts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.graphics.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.liftoff.data.dto.ExerciseDto
import com.example.liftoff.data.dto.WorkoutDto
import com.example.liftoff.data.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun WorkoutsScreen(db : WorkoutRepository) {
    var workouts by remember { mutableStateOf(listOf<WorkoutDto>()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            // for now, we hard code fetching workouts done by userId 6
            // db.createWorkout(WorkoutDto(6,"timed","test before commit",15))
            workouts = db.getWorkoutsByUserId(userId = 6).reversed()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.TopStart) {
        Text(
            text = "My Workouts",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(top = 16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp)
            .verticalScroll(rememberScrollState())
    ) {
        workouts.forEach { workout ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = workout.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )

                    Text(
                        text = workout.date,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (workout.exercises.isNotEmpty()) {
                        val strengthExercises = workout.exercises.filter { it.type == "strength" }
                        val timedExercises = workout.exercises.filter { it.type == "timed" }

                        // Group strength exercises together
                        if (strengthExercises.isNotEmpty()) {
                            StrengthExercises(strengthExercises)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Group timed exercises together
                        if (timedExercises.isNotEmpty()) {
                            TimedExercises(timedExercises)
                        }
                    } else {
                        Text("No exercises were found for ${workout.name}", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun StrengthExercises(strengthExercises: List<ExerciseDto>) {
    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text(text = "Name", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text(text = "Reps", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
            Text(text = "Sets", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
        }

        strengthExercises.forEach { exercise ->
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                Text(text = exercise.name, modifier = Modifier.weight(1f))
                Text(text = exercise.reps?.toString() ?: "N/A", modifier = Modifier.weight(0.5f))
                Text(text = exercise.sets?.toString() ?: "N/A", modifier = Modifier.weight(0.5f))
            }
        }
    }
}

@Composable
fun TimedExercises(timedExercises: List<ExerciseDto>) {
    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text(text = "Name", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text(text = "Duration", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }

        timedExercises.forEach { exercise ->
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                Text(text = exercise.name, modifier = Modifier.weight(1f))
                Text(text = "${exercise.duration} min", modifier = Modifier.weight(1f))
            }
        }
    }
}
