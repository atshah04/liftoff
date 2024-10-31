package com.example.liftoff.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
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
import com.example.liftoff.ui.login.LoginScreen


class GlobalState (val loggedIn: Boolean, val username: String) {
}

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier, gs: GlobalState, setGS: (GlobalState) -> Unit) {
    NavHost(navController, startDestination = "login", modifier = modifier) {
        composable("login") { LoginScreen({
            navController.navigate("home")
        }, { state -> setGS(state)  }) }
        composable(BottomNavItem.Home.route) { HomeScreen(gs) }
        composable(BottomNavItem.Todo.route) { TodoScreen() }
        composable(BottomNavItem.Workouts.route) { WorkoutsScreen(WorkoutRepository(supabase)) }
        composable(BottomNavItem.Generate.route) { GenerateScreen() }
        composable(BottomNavItem.Friends.route) { FriendsScreen() }
    }
}
