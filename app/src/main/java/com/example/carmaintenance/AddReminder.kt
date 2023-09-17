package com.example.carmaintenance

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
fun AddReminder(navController: NavHostController) {
    val context: Context = LocalContext.current
    val db = CarDatabase.getDatabase(LocalContext.current)
    val carDao = db.carsDao()
    var cars: List<Car>? = null

    runBlocking {
        launch {
            cars = carDao.getAllCars()
        }
    }

    if (cars == null || cars?.size == 0) {
        cars = listOf(Car(carID = -1, name = "No cars", year = null, make = null, model = null, mileage = null))
    }

    var carID = -1; var name = ""; var notes = ""; var date = ""; var time = ""; var mileage = ""

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add New Reminder",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("reminders_home") }) {
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
                    var carIDInput by remember { mutableIntStateOf(carID) }
                    var carName by remember { mutableStateOf("Select a car") }
                    var expanded by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            }
                        ) {
                            TextField(
                                value = carName,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                cars!!.forEach { car ->
                                    DropdownMenuItem(
                                        text = { Text(car.name.toString()) },
                                        onClick = {
                                            carName = car.name.toString()
                                            carIDInput = car.carID
                                            carID = carIDInput
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var nameInput by remember { mutableStateOf("") }
                    CustomTextField(label = "Name", value = nameInput, onInputChange = { nameInput = it; name = nameInput }, type = "String", singleLine = true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var notesInput by remember { mutableStateOf("") }
                    CustomTextField(label = "Notes", value = notesInput, onInputChange = { notesInput = it; notes = notesInput }, type = "String", singleLine = false)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var dateInput by remember { mutableStateOf("") }
                    CustomTextField(label = "Date", value = dateInput, onInputChange = { dateInput = it; date = dateInput }, type = "String", singleLine = true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var timeInput by remember { mutableStateOf("") }
                    CustomTextField(label = "Time", value = timeInput, onInputChange = { timeInput = it; time = timeInput }, type = "String", singleLine = true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var mileageInput by remember { mutableStateOf("") }
                    CustomTextField(label = "Mileage", value = mileageInput, onInputChange = { mileageInput = it; mileage = mileageInput }, type = "Int", singleLine = true)
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
                                    addReminder(context, carID, name, notes, date, time, mileage)
                                }
                            }

                            navController.navigate(route = "reminders_home")
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

suspend fun addReminder(context: Context, carID: Int, name: String, notes: String, date: String, time: String, mileage: String) {
    val db = CarDatabase.getDatabase(context)
    val remindersDao = db.remindersDao()
    remindersDao.insertReminder(Reminder(reminderID = 0, carID = carID, name = name, notes = notes, date = date, time = time, mileage = mileage))
}