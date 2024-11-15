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
import com.example.liftoff.ui.newacc.NewAccScreen
import com.example.liftoff.ui.options.OptionsScreen

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier, gs: GlobalState, setGS: (GlobalState) -> Unit) {
    val supabase = SupabaseService.client
    val sharedViewModel = WorkoutsTodoViewModel()
    val routes = listOf("options", "home", "login", "newAcc")
    val gsWrapper = { state: GlobalState -> setGS(state)  }
    val navFuncs: Map<String, () -> Unit> = routes.associate { it to {navController.navigate(it)}}
    NavHost(navController, startDestination = "options", modifier = modifier) {
        composable("options") { OptionsScreen(navFuncs, gsWrapper) }
        composable("login") { LoginScreen(navFuncs, gsWrapper) }
        composable("newAcc") { NewAccScreen(navFuncs, gsWrapper) }
        composable(BottomNavItem.Home.route) { HomeScreen(navFuncs, gs, gsWrapper) }
        composable(BottomNavItem.Todo.route) { TodoScreen(navController, sharedViewModel, gs) }
        composable(BottomNavItem.Workouts.route) { WorkoutsScreen(WorkoutRepository(supabase), gs) }
        composable(BottomNavItem.Generate.route) { GenerateScreen() }
        composable(BottomNavItem.Friends.route) { FriendsScreen(FriendsRepository(supabase), WorkoutRepository(supabase), gs) }

        composable("workout_input") {
            TodoInputScreen (sharedViewModel) { exerciseDto -> navController.popBackStack()  }
        }
    }
}
