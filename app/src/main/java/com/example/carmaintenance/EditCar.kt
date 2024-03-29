package com.example.carmaintenance

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCar(navController: NavHostController, carID: Int) {
    val context: Context = LocalContext.current
    val db = CarDatabase.getDatabase(context)
    val carsDao = db.carsDao()
    var car: Car? = null

    runBlocking {
        launch {
            car = carsDao.getCar(carID)
        }
    }

    var name = ""; var year = ""; var make = ""; var model = ""; var mileage = ""
    if (car != null) {
        name = car!!.name.toString()
        year = car!!.year.toString()
        make = car!!.make.toString()
        model = car!!.model.toString()
        mileage = car!!.mileage.toString()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Edit Car",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route = "specific_car/$carID") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to car"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            runBlocking {
                                launch {
                                    deleteCar(context, carID)
                                }
                            }

                            navController.navigate("cars_home")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Car"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var nameInput by remember { mutableStateOf(name) }
                    CustomTextField(label = "Name", value = nameInput, onInputChange = { nameInput = it; name = nameInput }, type = "String", singleLine = true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var yearInput by remember { mutableStateOf(year) }
                    CustomTextField(label = "Year", value = yearInput, onInputChange = { yearInput = it; year = yearInput }, type = "Int", singleLine = true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var makeInput by remember { mutableStateOf(make) }
                    CustomTextField(label = "Make", value = makeInput, onInputChange = { makeInput = it; make = makeInput }, type = "String", singleLine = true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var modelInput by remember { mutableStateOf(model) }
                    CustomTextField(label = "Model", value = modelInput, onInputChange = { modelInput = it; model = modelInput }, type = "String", singleLine = true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var mileageInput by remember { mutableStateOf(mileage) }
                    CustomTextField(label = "Mileage", value = mileageInput, onInputChange = { mileageInput = it; mileage = mileageInput }, type = "Int", singleLine = true)
                }
            }

            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            runBlocking {
                                launch {
                                    updateCar(context, carID, name, year, make, model, mileage)
                                }
                            }

                            navController.navigate(route = "specific_car/$carID")
                        }
                    ) {
                        Text("Update Car")
                    }
                }
            }
        }
    }
}

suspend fun updateCar(context: Context, carID: Int, name: String, year: String, make: String, model: String, mileage: String) {
    val db = CarDatabase.getDatabase(context)
    val carsDao = db.carsDao()
    carsDao.updateCar(carID, name, year, make, model, mileage)
}

suspend fun deleteCar(context: Context, carID: Int) {
    val db = CarDatabase.getDatabase(context)
    val carsDao = db.carsDao()
    carsDao.deleteCar(carID)
}