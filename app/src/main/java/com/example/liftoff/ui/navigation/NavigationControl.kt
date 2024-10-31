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
import com.example.liftoff.ui.todo.WorkoutsTodoViewModel
import com.example.liftoff.ui.todo.ExerciseTodo
import com.example.liftoff.ui.workouts.WorkoutsScreen
import com.example.liftoff.ui.todoinput.TodoInputScreen
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier) {
    val supabase = SupabaseService.client
    val sharedViewModel = WorkoutsTodoViewModel()

    NavHost(navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) { HomeScreen() }
        composable(BottomNavItem.Todo.route) { TodoScreen(navController, sharedViewModel, WorkoutRepository(supabase)) }
        composable(BottomNavItem.Workouts.route) { WorkoutsScreen(WorkoutRepository(supabase)) }
        composable(BottomNavItem.Generate.route) { GenerateScreen() }
        composable(BottomNavItem.Friends.route) { FriendsScreen() }

        composable("workout_input") {
            TodoInputScreen (sharedViewModel) { exerciseDto -> navController.popBackStack()  }
        }
    }
}
