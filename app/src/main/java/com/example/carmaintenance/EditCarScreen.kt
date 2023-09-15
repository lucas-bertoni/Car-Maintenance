package com.example.carmaintenance

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCarScreen(navController: NavHostController, carID: Int) {
    val context: Context = LocalContext.current
    val db = CarDatabase.getDatabase(context)
    val carsDao = db.carsDao()
    var car: Car? = null

    runBlocking {
        launch {
            car = carsDao.getCar(carID)
        }
    }

    var name = ""; var year = ""; var make = ""; var model = ""; var mileage = "";
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

                            navController.navigate("home/true")
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
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = nameInput,
                        onValueChange = {
                            nameInput = it
                            name = nameInput
                        },
                        label = { Text("Name") }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var yearInput by remember { mutableStateOf(year) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = yearInput,
                        onValueChange = {
                            if (it.length <= 4) {
                                yearInput = it
                                year = yearInput
                            }
                        },
                        label = { Text("Year") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var makeInput by remember { mutableStateOf(make) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = makeInput,
                        onValueChange = {
                            makeInput = it
                            make = makeInput
                        },
                        label = { Text("Make") }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var modelInput by remember { mutableStateOf(model) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = modelInput,
                        onValueChange = {
                            modelInput = it
                            model = modelInput
                        },
                        label = { Text("Model") }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var mileageInput by remember { mutableStateOf(mileage) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = mileageInput,
                        onValueChange = {
                            mileageInput = it
                            mileage = mileageInput
                        },
                        label = { Text("Current Mileage") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
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