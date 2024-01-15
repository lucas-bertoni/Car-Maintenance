package com.example.carmaintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RemindersViewModelFactory(private val remindersDao: RemindersDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemindersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RemindersViewModel(remindersDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}