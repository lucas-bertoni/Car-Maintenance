package com.example.carmaintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarsHome(navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Cars",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            item {
                CarListDisplay(navController, innerPadding)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            navController.navigate(route = "add_car")
                        }
                    ) {
                        Text("Add New Car")
                    }
                }
            }
        }
    }
}

@Composable
fun CarListDisplay(navController: NavHostController, innerPadding: PaddingValues) {
    val db = CarDatabase.getDatabase(LocalContext.current)
    val carDao = db.carsDao()
    var cars: List<Car>? = null

    runBlocking {
        launch {
            cars = carDao.getAllCars()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if (cars?.size == 0) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("No cars")
            }
        } else {
            cars?.forEach { it ->
                CarRow(it, navController)
            }
        }
    }
}

@Composable
fun CarRow(car: Car, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 5.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val carID = car.carID
        val name = car.name
        val year = car.year
        val make = car.make
        val model = car.model
        Button(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(5.dp),
            onClick = {
                navController.navigate("specific_car/$carID")
            }
        ) {
            if (name != "") name?.let { Text(it) } else Text("$year $make $model")
        }
    }
}