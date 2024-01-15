package com.example.carmaintenance

import androidx.lifecycle.ViewModel

class RemindersViewModel(private val remindersDao: RemindersDao): ViewModel() {
    fun allReminders(): List<Reminder> = remindersDao.allReminders()

    fun getReminder(reminderID: Int): Reminder = remindersDao.getReminder(reminderID)
}