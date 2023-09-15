package com.example.carmaintenance

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCar(navController: NavHostController) {
    val context: Context = LocalContext.current
    var name = ""; var year = ""; var make = ""; var model = ""; var mileage = ""

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add New Car",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("cars_home") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to home"
                        )
                    }
                },
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
                    val focusManager = LocalFocusManager.current
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = nameInput,
                        onValueChange = {
                            nameInput = it
                            name = nameInput
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        label = { Text("Name") },
                        singleLine = true
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
                    val focusManager = LocalFocusManager.current
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = yearInput,
                        onValueChange = {
                            if (it.length <= 4) {
                                yearInput = it
                                year = yearInput
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        label = { Text("Year") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
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
                    val focusManager = LocalFocusManager.current
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = makeInput,
                        onValueChange = {
                            makeInput = it
                            make = makeInput
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        label = { Text("Make") },
                        singleLine = true
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
                    val focusManager = LocalFocusManager.current
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = modelInput,
                        onValueChange = {
                            modelInput = it
                            model = modelInput
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        label = { Text("Model") },
                        singleLine = true
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var mileageInput by remember { mutableStateOf("") }
                    val focusManager = LocalFocusManager.current
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = mileageInput,
                        onValueChange = {
                            mileageInput = it
                            mileage = mileageInput
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        label = { Text("Current Mileage") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(0.75f),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            runBlocking {
                                launch {
                                    addCar(context, name, year, make, model, mileage)
                                }
                            }
                            navController.navigate(route = "cars_home")
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

suspend fun addCar(context: Context, name: String, year: String, make: String, model: String, mileage: String) {
    val db = CarDatabase.getDatabase(context)
    val carsDao = db.carsDao()
    carsDao.insertCar(Car(carID = 0, name = name, year = year, make = make, model = model, mileage = mileage))
}