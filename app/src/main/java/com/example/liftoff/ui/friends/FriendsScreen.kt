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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.example.liftoff.data.classes.User
import com.example.liftoff.data.dto.ExerciseDto
import com.example.liftoff.data.dto.FriendDto
import com.example.liftoff.data.dto.WorkoutDto
import com.example.liftoff.data.repository.FriendsRepository
import com.example.liftoff.data.repository.WorkoutRepository
import com.example.liftoff.data.viewmodel.FriendsViewModel
import com.example.liftoff.data.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

@Composable
fun FriendsScreen(dbf: FriendsRepository, dbw: WorkoutRepository, mvm: MainViewModel, fvm : FriendsViewModel) {
    val gs by mvm.gs.collectAsState()
    val setFriends = { friendsList: List<FriendDto> -> fvm.setFriends(friendsList) }
    val setFilteredFriends = { filteredList: List<FriendDto> -> fvm.setFilteredFriends(filteredList) }
    val setSearchVisibility = { isVisible: Boolean -> fvm.setSearchVisibility(isVisible) }
    val setSearchText = { searchText: TextFieldValue -> fvm.setSearchText(searchText) }
    val setIsAddingFriend = { isAdding: Boolean -> fvm.setIsAddingFriend(isAdding) }
    val friends by fvm.friends.collectAsState()
    val filteredFriends by fvm.filteredFriends.collectAsState()
    val isSearchVisible by fvm.isSearchVisible.collectAsState()
    val search by fvm.search.collectAsState()
    val isAddingFriend by fvm.isAddingFriend.collectAsState()

    LaunchedEffect(gs.userId) {
        setFriends(dbf.getFriendsByUserId(userId = gs.userId))
        setFilteredFriends(filteredFriends)
    }

    LaunchedEffect(search) {
        setFilteredFriends(if (search.text.isEmpty()) friends else {
            friends.filter { it.friendUsername.contains(search.text, ignoreCase = true) }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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

            IconButton(onClick = { setSearchVisibility(!isSearchVisible) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for Friends",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        if (isSearchVisible) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = search,
                    onValueChange = { setSearchText(it) },
                    placeholder = { Text(text = "Search...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true
                )

                Button(onClick = {
                    setIsAddingFriend(true)

                    CoroutineScope(Dispatchers.IO).launch {
                        val results = com.example.liftoff.ui.login.supabase.from("users")
                            .select(columns = Columns.list("id", "username", "password")) {
                                filter {
                                    eq("username", search.text)
                                }
                            }
                        val users = results.decodeList<User>()

                        if (users.isNotEmpty()) {
                            val friendId = users.first().id

                            val newFriend = FriendDto(gs.userId, friendId = friendId, friendUsername = search.text)
                            dbf.addFriend(newFriend)

                            setFriends(dbf.getFriendsByUserId(userId = gs.userId))
                            setFilteredFriends(filteredFriends)
                        }
                        setIsAddingFriend(false)
                    }
                }) {
                    Text(text = "Add Friend")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
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
                                if (longestExercise == null || (exercise.duration ?: 0) > (longestExercise?.duration ?: 0)
                                ) {
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
                        var hl = "-"
                        var le = "-"
                        var tt = "-"
                        var ws = "-"
                        if (heaviestLift != null) {
                            hl = "${heaviestLift?.name} ${heaviestLift?.weight}"
                        }
                        if (longestExercise != null) {
                            le = "${longestExercise?.name} ${longestExercise?.duration}"
                        }
                        Column {
                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                Text(
                                    text = "Heaviest Lift: " + hl,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "Longest Workout: " + le,
                                    modifier = Modifier.weight(0.5f)
                                )
                                Text(
                                    text = "Time Spent: $totalTime",
                                    modifier = Modifier.weight(0.5f)
                                )
                                Text(
                                    text = "Total Workouts: ${workouts.size}",
                                    modifier = Modifier.weight(0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
