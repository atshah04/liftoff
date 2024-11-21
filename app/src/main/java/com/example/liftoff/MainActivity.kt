package com.example.liftoff

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.liftoff.ui.navigation.NavHostContainer
import androidx.navigation.compose.rememberNavController
import com.example.liftoff.ui.navigation.BottomNavigationBar
import androidx.compose.runtime.*
import com.example.liftoff.data.viewmodel.*

class MainActivity : ComponentActivity() {
    private val mvm: MainViewModel by viewModels()
    private val lgvm : LoginViewModel by viewModels()
    private val navm : LoginViewModel by viewModels()
    private val fvm : FriendsViewModel by viewModels()
    private val gvm: GenerateViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        super.onCreate(savedInstanceState)
        setContent {
            val gs = mvm.gs.collectAsState()
            val navController = rememberNavController()
            if (gs.value.loggedIn) {
                Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
                    NavHostContainer(navController, Modifier.padding(innerPadding), mvm, lgvm, navm, fvm, gvm)
                }
            } else NavHostContainer(navController, Modifier.padding(), mvm, lgvm, navm, fvm, gvm)
        }
    }
}
