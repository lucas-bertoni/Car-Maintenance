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

@Database(entities = [Car::class, ServiceRecord::class, Reminder::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carsDao(): CarsDao
    abstract fun serviceRecordsDao(): ServiceRecordsDao
    abstract fun remindersDao(): RemindersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
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

@Entity(tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = Car::class,
            parentColumns = ["carID"],
            childColumns = ["carID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reminder(
    @PrimaryKey(autoGenerate = true) val reminderID: Int,
    @ColumnInfo(name = "carID") val carID: Int?, // A reminder doesn't have to be linked to a car
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "mileage") val mileage: String?
)

@Dao
interface CarsDao {
    @Query("SELECT carID, name, year, make, model FROM cars")
    fun allCars(): List<Car>

    @Query("SELECT * FROM cars WHERE carID = :carID")
    fun getCar(carID: Int): Car

    @Insert
    fun insertCar(car: Car)

    @Query("DELETE FROM cars WHERE carID = :carID")
    fun deleteCar(carID: Int)

    @Query("UPDATE cars SET name = :name, year = :year, make = :make, model = :model, mileage = :mileage WHERE carID = :carID")
    fun updateCar(carID: Int, name: String, year: String, make: String, model: String, mileage: String)
}

@Dao
interface ServiceRecordsDao {
    @Query("SELECT * FROM service_records WHERE carID = :carID")
    fun allServiceRecords(carID: Int): List<ServiceRecord>

    @Query("SELECT * FROM service_records WHERE serviceRecordID = :serviceRecordID")
    fun getServiceRecord(serviceRecordID: Int): ServiceRecord

    @Insert
    fun insertServiceRecord(serviceRecord: ServiceRecord)

    @Query("DELETE FROM service_records WHERE serviceRecordID = :serviceRecordID")
    fun deleteServiceRecord(serviceRecordID: Int)

    @Query("UPDATE service_records SET name = :name, notes = :notes, mileage = :mileage, date = :date WHERE serviceRecordID = :serviceRecordID")
    fun updateServiceRecord(serviceRecordID: Int, name: String, notes: String, mileage: String, date: String)
}

@Dao
interface RemindersDao {
    @Query("SELECT * FROM reminders")
    fun allReminders(): List<Reminder>

    @Query("SELECT * FROM reminders WHERE reminderID = :reminderID")
    fun getReminder(reminderID: Int): Reminder

    @Insert
    fun insertReminder(reminder: Reminder)

    @Query("DELETE FROM reminders WHERE reminderID = :reminderID")
    fun deleteReminder(reminderID: Int)

    @Query("UPDATE reminders SET name = :name, notes = :notes, date = :date, time = :time, mileage = :mileage WHERE reminderID = :reminderID")
    fun updateReminder(reminderID: Int, name: String, notes: String, date: String, time: String, mileage: String)
}