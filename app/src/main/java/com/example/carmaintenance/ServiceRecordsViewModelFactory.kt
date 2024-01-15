package com.example.carmaintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ServiceRecordsViewModelFactory(private val serviceRecordsDao: ServiceRecordsDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceRecordsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceRecordsViewModel(serviceRecordsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}