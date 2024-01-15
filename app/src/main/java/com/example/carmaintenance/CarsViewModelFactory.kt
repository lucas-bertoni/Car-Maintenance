package com.example.carmaintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CarsViewModelFactory(private val carsDao: CarsDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarsViewModel(carsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}