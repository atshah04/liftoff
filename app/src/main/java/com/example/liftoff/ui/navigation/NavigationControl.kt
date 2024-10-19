package com.example.liftoff.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.liftoff.data.database.SupabaseService
import com.example.liftoff.data.repository.WorkoutRepository
import com.example.liftoff.ui.home.HomeScreen
import com.example.liftoff.ui.friends.FriendsScreen
import com.example.liftoff.ui.generate.GenerateScreen
import com.example.liftoff.ui.todo.TodoScreen
import com.example.liftoff.ui.workouts.WorkoutsScreen

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier) {
    val supabase = SupabaseService.client

    NavHost(navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) { HomeScreen() }
        composable(BottomNavItem.Todo.route) { TodoScreen() }
        composable(BottomNavItem.Workouts.route) { WorkoutsScreen(WorkoutRepository(supabase)) }
        composable(BottomNavItem.Generate.route) { GenerateScreen() }
        composable(BottomNavItem.Friends.route) { FriendsScreen() }
    }
}
