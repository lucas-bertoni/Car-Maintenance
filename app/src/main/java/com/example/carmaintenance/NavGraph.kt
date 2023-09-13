package com.example.carmaintenance

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(route = "home"){
            HomeScreen(navController)
        }
        composable(route = "specific_car"){
            SpecificCarScreen(navController)
        }
        composable(route = "add_car") {
            AddCarScreen(navController)
        }
    }
}