package com.example.carmaintenance

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecificServiceRecord(navController: NavHostController, serviceRecordID: Int) {
    val context: Context = LocalContext.current
    val db = CarDatabase.getDatabase(context)
    val serviceRecordsDao = db.serviceRecordsDao()
    var serviceRecord: ServiceRecord? = null

    runBlocking {
        launch {
            serviceRecord = serviceRecordsDao.getServiceRecord(serviceRecordID)
        }
    }

    val carID = serviceRecord?.carID    // This can't be null
    val name = serviceRecord?.name      // This can't be null
    val notes = if (serviceRecord?.notes == null) "" else serviceRecord?.notes
    val mileage = if (serviceRecord?.mileage == null) "" else serviceRecord?.mileage
    val date = serviceRecord?.date      // This can't be null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route = "specific_car/$carID") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to car"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(route = "edit_service_record/$serviceRecordID") }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit service record"
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
                        .background(Color.LightGray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Name: $name")
                    Text("Date: $date")
                    Text("Mileage: $mileage")
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                ) {
                    Text("Notes:")
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("$notes")
                }
            }
        }
    }
}