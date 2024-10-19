package com.example.liftoff.ui.workouts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.liftoff.data.dto.WorkoutDto
import com.example.liftoff.data.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val default_mod = Modifier
    .clip(RoundedCornerShape(2.dp))
    .border(1.dp, Color.Black)
    .padding(8.dp)
    .fillMaxWidth(0.9f)

@Composable
fun WorkoutsScreen(db : WorkoutRepository) {
    var workouts by remember { mutableStateOf(listOf<WorkoutDto>()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            // for now, we hard code fetching workouts done by userId 6
            // db.createWorkout(WorkoutDto(6,"timed","test before commit",15))
            workouts = db.getWorkoutsByUserId(userId = 6)
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(top = 80.dp)
        ) {
            DynamicColumn(items = workouts)
        }
    }
}

@Composable
fun Card(session: WorkoutDto) {
    Column(modifier = default_mod) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Exercise", modifier = Modifier.weight(1f))

            if (session.workoutType == "strength") {
                Text(text = "Reps", modifier = Modifier.weight(0.5f))
                Text(text = "Sets", modifier = Modifier.weight(0.5f))
            } else if (session.workoutType == "timed") {
                Text(text = "Duration", modifier = Modifier.weight(1f))
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = session.workoutName, modifier = Modifier.weight(1f))

            when (session.workoutType) {
                "strength" -> {
                    Text(text = session.reps?.toString() ?: "N/A", modifier = Modifier.weight(0.5f))
                    Text(text = session.sets?.toString() ?: "N/A", modifier = Modifier.weight(0.5f))
                }
                "timed" -> {
                    Text(text = session.duration?.toString() ?: "N/A", modifier = Modifier.weight(1f))
                }
                else -> {
                    Text(text = "Unknown Type", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun DynamicColumn(items: List<WorkoutDto>) {
    Column {
        items.forEach { session ->
                Card(session)
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}
