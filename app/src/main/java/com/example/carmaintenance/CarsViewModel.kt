package com.example.carmaintenance

import androidx.lifecycle.ViewModel

class CarsViewModel(private val carsDao: CarsDao): ViewModel() {
    fun allCars(): List<Car> = carsDao.allCars()

    fun getCar(carID: Int): Car = carsDao.getCar(carID)
}