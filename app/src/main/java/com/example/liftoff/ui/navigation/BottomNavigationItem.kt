package com.example.liftoff.ui.navigation

import com.example.liftoff.R

sealed class BottomNavItem(val icon: Int, val route: String) {
    data object Home : BottomNavItem(R.drawable.ic_home_black_24dp, "home")
    data object Todo : BottomNavItem(R.drawable.todo_icon, "todo")
    data object Workouts : BottomNavItem(R.drawable.workouts_icon, "workouts")
    data object Generate : BottomNavItem(R.drawable.generate_workouts_icon, "generate")
    data object Friends : BottomNavItem(R.drawable.friends_icon, "friends")
}
