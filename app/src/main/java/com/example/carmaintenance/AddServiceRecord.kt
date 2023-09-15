package com.example.carmaintenance

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceRecord(navController: NavHostController, carID: Int) {
    val context: Context = LocalContext.current

    var name = ""; var notes = ""; var mileage = ""; var date = "";

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add Service Record",
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
                    var notesInput by remember { mutableStateOf(notes) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = notesInput,
                        onValueChange = {
                            notesInput = it
                            notes = notesInput
                        },
                        label = { Text("Notes") }
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
                        label = { Text("Mileage") }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var dateInput by remember { mutableStateOf(date) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = dateInput,
                        onValueChange = {
                            dateInput = it
                            date = dateInput
                        },
                        label = { Text("Date") }
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
                                    if (name != "" && date != "") {
                                        addServiceRecord(context, carID, name, notes, mileage, date)
                                    } else {
                                        
                                    }

                                }
                            }

                            navController.navigate(route = "specific_car/$carID")
                        }
                    ) {
                        Text("Add Service Record")
                    }
                }
            }
        }
    }
}

suspend fun addServiceRecord(context: Context, carID: Int, name: String, notes: String, mileage: String, date: String) {
    val db = CarDatabase.getDatabase(context)
    val serviceRecordsDao = db.serviceRecordsDao()
    serviceRecordsDao.insertServiceRecord(ServiceRecord(serviceRecordID = 0, carID = carID, name = name, notes = notes, mileage = mileage, date = date))
}
