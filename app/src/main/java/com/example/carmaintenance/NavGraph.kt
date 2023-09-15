package com.example.carmaintenance

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "cars_home"
    ) {
        composable("cars_home") {it ->
            CarsHome(navController)
        }

        composable("add_car") {
            AddCar(navController)
        }

        composable("specific_car/{carID}") {it ->
            val carID = it.arguments?.getString("carID")
            if (carID != null) {
                SpecificCar(navController, carID = carID.toInt())
            }
        }

        composable("edit_car/{carID}") {
            val carID = it.arguments?.getString("carID")
            if (carID != null) {
                EditCar(navController, carID = carID.toInt())
            }
        }

        composable("specific_service_record/{serviceRecordID}") {
            val serviceRecordID = it.arguments?.getString("serviceRecordID")
            if (serviceRecordID != null) {
                SpecificServiceRecord(navController, serviceRecordID = serviceRecordID.toInt())
            }
        }

        composable("edit_service_record/{serviceRecordID}") {
            val serviceRecordID = it.arguments?.getString("serviceRecordID")
            if (serviceRecordID != null) {
                EditServiceRecord(navController, serviceRecordID = serviceRecordID.toInt())
            }
        }
    }
}