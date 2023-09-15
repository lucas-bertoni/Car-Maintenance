package com.example.carmaintenance

import android.content.Context
import androidx.room.Room
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Database(entities = [Car::class, ServiceRecord::class], version = 5)
abstract class CarDatabase : RoomDatabase() {
    abstract fun carsDao(): CarsDao
    abstract fun serviceRecordsDao(): ServiceRecordsDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null

        fun getDatabase(context: Context): CarDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "car_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) val carID: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "year") val year: String?,
    @ColumnInfo(name = "make") val make: String?,
    @ColumnInfo(name = "model") val model: String?,
    @ColumnInfo(name = "mileage") val mileage: String?
)

@Entity(tableName = "service_records",
    foreignKeys = [
        ForeignKey(
            entity = Car::class,
            parentColumns = ["carID"],
            childColumns = ["carID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ServiceRecord(
    @PrimaryKey(autoGenerate = true) val serviceRecordID: Int,
    @ColumnInfo(name = "carID") val carID: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "mileage") val mileage: String?,
    @ColumnInfo(name = "date") val date: String,
)

@Dao
interface CarsDao {
    @Query("SELECT carID, name, year, make, model FROM cars")
    suspend fun getAllCars(): List<Car>

    @Query("SELECT * FROM cars WHERE carID = :carID")
    suspend fun getCar(carID: Int): Car

    @Insert
    suspend fun insertCar(car: Car)

    @Query("DELETE FROM cars WHERE carID = :carID")
    suspend fun deleteCar(carID: Int)

    @Query("UPDATE cars SET name = :name, year = :year, make = :make, model = :model, mileage = :mileage WHERE carID = :carID")
    suspend fun updateCar(carID: Int, name: String, year: String, make: String, model: String, mileage: String)
}

@Dao
interface ServiceRecordsDao {
    @Query("SELECT * FROM service_records WHERE carID = :carID")
    suspend fun getServiceRecords(carID: Int): List<ServiceRecord>

    @Query("SELECT * FROM service_records WHERE serviceRecordID = :serviceRecordID")
    suspend fun getServiceRecord(serviceRecordID: Int): ServiceRecord

    @Insert
    suspend fun insertServiceRecord(serviceRecord: ServiceRecord)

    @Query("DELETE FROM service_records WHERE serviceRecordID = :serviceRecordID")
    suspend fun deleteServiceRecord(serviceRecordID: Int)

    @Query("UPDATE service_records SET name = :name, notes = :notes, mileage = :mileage, date = :date WHERE serviceRecordID = :serviceRecordID")
    suspend fun updateServiceRecord(serviceRecordID: Int, name: String, notes: String, mileage: String, date: String)
}