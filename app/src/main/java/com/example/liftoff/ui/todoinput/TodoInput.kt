package com.example.liftoff.ui.todoinput
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.ExposedDropdownMenuDefaults

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.liftoff.data.dto.ExerciseDto
import com.example.liftoff.ui.todo.ExerciseTodo
import com.example.liftoff.ui.todo.WorkoutsTodoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDropdownMenu(viewModel: WorkoutsTodoViewModel, onExerciseCreated: (ExerciseDto) -> Unit) {
    // State to hold the selected workout type
    var selectedWorkout by remember { mutableStateOf("") }
    // State to control the dropdown visibility
    var isExpanded by remember { mutableStateOf(false) }

    // List of workout options
    val workoutOptions = listOf("", "Timed", "Strength")

    // Exercise Dto Inputs
    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val exerciseDto = remember {
        derivedStateOf {
            ExerciseDto(
                name = name,
                type = if (selectedWorkout == "Timed") "timed" else "strength",
                duration = duration.toIntOrNull(),
                sets = sets.toIntOrNull(),
                reps = reps.toIntOrNull(),
                weight = weight.toDoubleOrNull()
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = !isExpanded }) {
                    // Text displaying the currently selected workout
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = selectedWorkout,
                        onValueChange = {},
                        label = { Text("Select Workout Type") },
                        trailingIcon = {
                            // Icon to indicate dropdown
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        }
                    )
                    // Dropdown menu containing workout options
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        workoutOptions.forEachIndexed { index, workout ->
                            DropdownMenuItem(
                                text = { Text(workout) },
                                onClick = {
                                    selectedWorkout = workoutOptions[index]
                                    isExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row {
                when (selectedWorkout) {
                    "Timed" -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Workout Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = duration,
                                onValueChange = { duration = it },
                                label = { Text("Duration in Minutes") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    "Strength" -> {
                        Column (modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Workout Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = sets,
                                onValueChange = { sets = it },
                                label = { Text("Number of Sets") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = reps,
                                onValueChange = { reps = it },
                                label = { Text("Reps per Set") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = weight,
                                onValueChange = { weight = it },
                                label = { Text("Weight in Pounds") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.addWorkout(ExerciseTodo(
                    name = name,
                    type = if (selectedWorkout == "Timed") "timed" else "strength",
                    duration = duration.toIntOrNull(),
                    sets = sets.toIntOrNull(),
                    reps = reps.toIntOrNull(),
                    weight = weight.toDoubleOrNull(),
                    isDone = false,
                    id = viewModel.curId
                ))
                val exerciseDto = ExerciseDto(
                    name = name,
                    type = if (selectedWorkout == "Timed") "timed" else "strength",
                    duration = duration.toIntOrNull(),
                    sets = sets.toIntOrNull(),
                    reps = reps.toIntOrNull(),
                    weight = weight.toDoubleOrNull()
                )
                onExerciseCreated(exerciseDto)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Save Exercise")
        }
    }
}


@Composable
fun TodoInputScreen(viewModel: WorkoutsTodoViewModel, onBack: (ExerciseDto)->Unit) {
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
        Text(
            text = "Add Workout",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        )
        {
           WorkoutDropdownMenu( viewModel , onExerciseCreated = { exerciseDto -> onBack(exerciseDto)})
        }
    }
}