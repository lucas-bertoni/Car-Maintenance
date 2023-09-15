package com.example.carmaintenance

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home/false"
    ) {
        composable("home/{intentional}") {it ->
            val intentional = it.arguments?.getString("intentional").toBoolean()
            if (intentional != null) {
                if (intentional) {
                    HomeScreen(navController, true)
                } else {
                    HomeScreen(navController, false)
                }
            }

        }
        composable("specific_car/{carID}") {it ->
            val carID = it.arguments?.getString("carID")
            if (carID != null) {
                SpecificCarScreen(navController, carID = carID.toInt())
            }
        }
        composable("add_car") {
            AddCarScreen(navController)
        }

        composable("edit_car/{carID}") {
            val carID = it.arguments?.getString("carID")
            if (carID != null) {
                EditCarScreen(navController, carID = carID.toInt())
            }
        }
    }
}