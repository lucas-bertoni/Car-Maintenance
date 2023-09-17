package com.example.carmaintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersHome(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Reminders",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("cars_home") }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Cars home"
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
                ReminderListDisplay(navController)
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
                            navController.navigate(route = "add_reminder")
                        }
                    ) {
                        Text("Add New Reminder")
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderListDisplay(navController: NavHostController) {
    val db = CarDatabase.getDatabase(LocalContext.current)
    val remindersDao = db.remindersDao()
    var reminders: List<Reminder>? = null

    runBlocking {
        launch {
            reminders = remindersDao.getReminders()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if (reminders?.size == 0) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("No reminders")
            }
        } else {
            reminders?.forEach {
                ReminderRow(it, navController)
            }
        }
    }
}

@Composable
fun ReminderRow(reminder: Reminder, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 5.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val reminderID = reminder.reminderID
        val name = reminder.name
//        val date = reminder.date
//        val mileage = reminder.mileage
        Button(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(5.dp),
            onClick = {
                navController.navigate("specific_reminder/$reminderID")
            }
        ) {
            Text(name)
        }
    }
}