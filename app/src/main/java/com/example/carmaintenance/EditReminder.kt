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
fun EditReminder(navController: NavHostController, reminderID: Int) {
    val context: Context = LocalContext.current
    val db = CarDatabase.getDatabase(context)
    val remindersDao = db.remindersDao()
    var reminder: Reminder? = null

    runBlocking {
        launch {
            reminder = remindersDao.getReminder(reminderID)
        }
    }

    var name = ""; var notes = ""; var date = ""; var time = ""; var mileage = ""
    if (reminder != null) {
        name = reminder!!.name
        notes = if (reminder!!.notes == null) "" else reminder!!.notes.toString()
        date = if (reminder!!.date == null) "" else reminder!!.date.toString()
        time = if (reminder!!.time == null) "" else reminder!!.time.toString()
        mileage = reminder!!.mileage.toString()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Edit Reminder",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route = "specific_reminder/$reminderID") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to reminder"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            runBlocking {
                                launch {
                                    deleteReminder(context, reminderID)
                                }
                            }

                            navController.navigate("reminders_home")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete reminder"
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
                    var notesInput by remember { mutableStateOf(notes) }
                    CustomTextField(label = "Notes", value = notesInput, onInputChange = { notesInput = it; notes = notesInput }, type = "String", singleLine = false)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var dateInput by remember { mutableStateOf(date) }
                    CustomTextField(label = "Date", value = dateInput, onInputChange = { dateInput = it; date = dateInput }, type = "String", singleLine = true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    var timeInput by remember { mutableStateOf(time) }
                    CustomTextField(label = "Time", value = timeInput, onInputChange = { timeInput = it; time = timeInput }, type = "String", singleLine = true)
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
                                    updateReminder(context, reminderID, name, notes, date, time, mileage)
                                }
                            }

                            navController.navigate(route = "reminders_home")
                        }
                    ) {
                        Text("Update Reminder")
                    }
                }
            }
        }
    }
}

suspend fun updateReminder(context: Context, reminderID: Int, name: String, notes: String, date: String, time: String, mileage: String) {
    val db = CarDatabase.getDatabase(context)
    val remindersDao = db.remindersDao()
    remindersDao.updateReminder(reminderID, name, notes, mileage, time, date)
}

suspend fun deleteReminder(context: Context, reminderID: Int) {
    val db = CarDatabase.getDatabase(context)
    val remindersDao = db.remindersDao()
    remindersDao.deleteReminder(reminderID)
}