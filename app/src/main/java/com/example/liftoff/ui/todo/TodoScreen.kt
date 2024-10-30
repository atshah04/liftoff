package com.example.liftoff.ui.todo

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material3.SwipeToDismissBox
import androidx.navigation.NavHostController
import com.example.liftoff.data.dto.ExerciseDto
import androidx.compose.foundation.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun WorkoutTodoCard (item: ExerciseTodo) {
    Card (
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors (
            containerColor = if (item.isDone) Color(0xFF98EC99) else Color.White
        )) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (item.type == "timed") {
                Text (
                    text = "Duration: ${item.duration} mins",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            } else {
                Text (
                    text = "Sets: ${item.sets}          Reps: ${item.reps}          Weight: ${item.weight} lbs",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
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
        }
    }
}

@Composable
fun TodoScreen(navController: NavHostController, viewModel: WorkoutsTodoViewModel) {

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
    }
}
