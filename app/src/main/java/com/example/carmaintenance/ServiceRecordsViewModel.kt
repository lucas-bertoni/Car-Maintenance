package com.example.carmaintenance

import androidx.lifecycle.ViewModel

class ServiceRecordsViewModel(private val serviceRecordsDao: ServiceRecordsDao): ViewModel() {
    fun allServiceRecords(carID: Int): List<ServiceRecord> = serviceRecordsDao.allServiceRecords(carID)

    fun getServiceRecord(serviceRecordID: Int): ServiceRecord = serviceRecordsDao.getServiceRecord(serviceRecordID)
}