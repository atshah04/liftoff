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
import com.example.liftoff.ui.workouts.WorkoutsScreen
import com.example.liftoff.ui.todoinput.TodoInputScreen
import com.example.liftoff.ui.login.LoginScreen
import com.example.liftoff.data.classes.GlobalState
import com.example.liftoff.data.repository.FriendsRepository
import com.example.liftoff.data.viewmodel.*
import com.example.liftoff.ui.newacc.NewAccScreen
import com.example.liftoff.ui.options.OptionsScreen

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier, mvm: MainViewModel, lgvm: LoginViewModel, navm: LoginViewModel,
                    fvm: FriendsViewModel, gvm: GenerateViewModel) {
    val supabase = SupabaseService.client
    val sharedViewModel = WorkoutsTodoViewModel()
    val routes = listOf("options", "home", "login", "newAcc")
    val navFuncs: Map<String, () -> Unit> = routes.associate { it to {navController.navigate(it)}}
    NavHost(navController, startDestination = "options", modifier = modifier) {
        composable("options") { OptionsScreen(navFuncs) }
        composable("login") { LoginScreen(navFuncs, mvm, lgvm) }
        composable("newAcc") { NewAccScreen(navFuncs, mvm, navm) }
        composable(BottomNavItem.Home.route) { HomeScreen(navFuncs, mvm) }
        composable(BottomNavItem.Todo.route) { TodoScreen(navController, sharedViewModel, mvm) }
        composable(BottomNavItem.Workouts.route) { WorkoutsScreen(WorkoutRepository(supabase), mvm) }
        composable(BottomNavItem.Generate.route) { GenerateScreen(navFuncs, gvm) }
        composable(BottomNavItem.Friends.route) { FriendsScreen(FriendsRepository(supabase), WorkoutRepository(supabase), mvm, fvm) }
        composable("workout_input") {
            TodoInputScreen (sharedViewModel) { exerciseDto -> navController.popBackStack()  }
        }
    }
}
