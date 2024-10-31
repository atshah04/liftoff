package com.example.liftoff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.liftoff.ui.navigation.NavHostContainer
import androidx.navigation.compose.rememberNavController
import com.example.liftoff.ui.navigation.BottomNavigationBar
import com.example.liftoff.data.classes.GlobalState
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val (gs, setGS) = remember { mutableStateOf(GlobalState(false, "", -1))}
            if (gs.loggedIn) {
                Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
                    NavHostContainer(navController, Modifier.padding(innerPadding), gs, setGS)
                }
            } else NavHostContainer(navController, Modifier.padding(), gs, setGS)
        }
    }
}
