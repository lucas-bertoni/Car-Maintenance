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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecificCar(navController: NavHostController, carID: Int) {
    val context: Context = LocalContext.current
    val db = CarDatabase.getDatabase(context)
    val carsDao = db.carsDao()
    val serviceRecordsDao = db.serviceRecordsDao()
    var car: Car? = null
    var serviceRecords: List<ServiceRecord>? = null

    runBlocking {
        launch {
            car = carsDao.getCar(carID)
            serviceRecords = serviceRecordsDao.getServiceRecords(carID)
        }
    }

    var title = ""
    var name = ""; var year = ""; var make = ""; var model = ""; var mileage = ""
    if (car != null) {
        title = if(car!!.name != "") car!!.name.toString() else "${car!!.year} ${car!!.make} ${car!!.model}"
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
                        "$title",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route = "cars_home") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to home"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(route = "edit_car/$carID") }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit car"
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
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("$year $make $model")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("$mileage miles")
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                ) {
                    serviceRecords?.forEach { it ->
                        ServiceRecordRow(it, navController)
                    }
                }
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
                            navController.navigate(route = "add_service_record/$carID")
                        }
                    ) {
                        Text("Add Service Record")
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceRecordRow(serviceRecord: ServiceRecord, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${serviceRecord.name} ${serviceRecord.date}")
        IconButton(onClick = { navController.navigate(route = "specific_service_record/${serviceRecord.serviceRecordID}") }) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "View service record"
            )
        }
    }
}