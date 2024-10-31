package com.example.liftoff.ui.todo

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.SwipeToDismissBox
import androidx.navigation.NavHostController
import com.example.liftoff.data.dto.ExerciseDto
import com.example.liftoff.data.repository.WorkoutRepository
import kotlin.collections.*
import com.example.liftoff.data.dto.WorkoutDto
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*
import com.example.liftoff.data.database.SupabaseService
import com.example.liftoff.ui.components.*

fun ExerciseTodo.toExerciseDto(): ExerciseDto {
    return ExerciseDto(
        name = this.name,
        type = this.type,
        duration = this.duration,
        sets = this.sets,
        reps = this.reps,
        weight = this.weight
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground (dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFFF58484)
        SwipeToDismissBoxValue.EndToStart -> Color(0xFF98EC99)
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon (
            imageVector = Icons.Default.Delete,
            contentDescription = "delete"
        )

        Spacer (modifier = Modifier)

        Icon (
            imageVector = Icons.Default.Done,
            contentDescription = "Done"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoItemRow (item : ExerciseTodo, viewModel : WorkoutsTodoViewModel) {

    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = { DismissBackground(dismissState) },
        content = { WorkoutTodoCard(item) }
    )

    LaunchedEffect(dismissState.dismissDirection) {
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
            viewModel.removeTodo(item)
            kotlinx.coroutines.delay(100)
            dismissState.reset()
        } else if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            viewModel.toggleComplete(item)
            kotlinx.coroutines.delay(100)
            dismissState.reset()
        } else {
            dismissState.reset()
        }
    }
}

@Composable
fun TodoScreen(navController: NavHostController, viewModel: WorkoutsTodoViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "To Do",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            IconButton (onClick = {
                navController.navigate("workout_input")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Workout",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        LazyColumn {
            items(viewModel.todoItems) { item ->
                ToDoItemRow(item, viewModel)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val exerciseDtoList: List<ExerciseDto> = viewModel.todoItems.map{it.toExerciseDto()}
                val workout: WorkoutDto = WorkoutDto(
                    userId = 6,
                    name = "My Workout",
                    exercises = exerciseDtoList,
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
                )

                val supabase = SupabaseService.client
                val db: WorkoutRepository = WorkoutRepository(supabase)

                coroutineScope.launch {
                    db.createWorkout(workout)
                }
                viewModel.todoItems.clear()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Save Workout")
        }
    }
}
