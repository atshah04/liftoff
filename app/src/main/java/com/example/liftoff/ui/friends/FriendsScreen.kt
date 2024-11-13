package com.example.liftoff.ui.friends

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.liftoff.data.classes.GlobalState
import com.example.liftoff.data.dto.ExerciseDto
import com.example.liftoff.data.dto.FriendDto
import com.example.liftoff.data.dto.WorkoutDto
import com.example.liftoff.data.repository.FriendsRepository
import com.example.liftoff.data.repository.WorkoutRepository

@Composable
fun SearchButtonWithBar() {
    var isSearchVisible by remember { mutableStateOf(false) }

    Column {
        // Button to toggle the search bar visibility
        Button(onClick = { isSearchVisible = !isSearchVisible }) {
            Text(text = "Show/Hide Search")
        }

        // Search bar visibility controlled by isSearchVisible
        if (isSearchVisible) {
            TextField(
                value = "",
                onValueChange = { /* Handle search query */ },
                placeholder = { Text(text = "Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun FriendsScreen(dbf : FriendsRepository, dbw : WorkoutRepository, gs: GlobalState) {
    var friends by remember { mutableStateOf(listOf<FriendDto>()) }
    var isSearchVisible by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    LaunchedEffect(gs.userId) {
        friends = dbf.getFriendsByUserId(userId = gs.userId)
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start)
    {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Friends",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for Friends",
                    modifier = Modifier.size(32.dp)
                )
            }

            if (isSearchVisible) {
                TextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text(text = "Search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp)
            .verticalScroll(rememberScrollState())
    ) {
        friends.forEach { friend ->
            var workouts by remember { mutableStateOf(listOf<WorkoutDto>()) }
            var totalTime by remember { mutableStateOf(0) }
            var heaviestLift by remember { mutableStateOf<ExerciseDto?>(null) }
            var longestExercise by remember { mutableStateOf<ExerciseDto?>(null) }

            LaunchedEffect(friend.friendId) {
                workouts = dbw.getWorkoutsByUserId(userId = friend.friendId)
                totalTime = 0
                heaviestLift = null
                longestExercise = null

                workouts.forEach { workout ->
                    workout.exercises.forEach { exercise ->
                        if (exercise.type == "strength") {
                            if (heaviestLift == null || (exercise.weight ?: 0.0) > (heaviestLift?.weight ?: 0.0)) {
                                heaviestLift = exercise
                            }
                        } else {
                            totalTime += (exercise.duration ?: 0)
                            if (longestExercise == null || (exercise.duration ?: 0) > (longestExercise?.duration ?: 0)) {
                                longestExercise = exercise
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = friend.friendUsername,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )

                    Column {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                            Text(
                                text = "Heaviest Lift: " + heaviestLift?.name + " " + heaviestLift?.weight,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Longest Workout: " + longestExercise?.name + " " + longestExercise?.duration,
                                modifier = Modifier.weight(0.5f)
                            )
                            Text(
                                text = "Time Spent: $totalTime",
                                modifier = Modifier.weight(0.5f)
                            )
                            Text(
                                text = "Total Workouts: " + workouts.size,
                                modifier = Modifier.weight(0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}
