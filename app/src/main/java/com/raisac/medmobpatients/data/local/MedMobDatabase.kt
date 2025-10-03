package com.raisac.medmobpatients.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raisac.medmobpatients.data.local.converter.StringListConverter
import com.raisac.medmobpatients.data.local.dao.*
import com.raisac.medmobpatients.data.local.entity.*

/**
 * Room database for MedMobPatients app
 * Stores all patient health data locally
 */
@Database(
    entities = [
        UserEntity::class,
        HealthRecordEntity::class,
        MedicationEntity::class,
        AppointmentEntity::class,
        VitalSignsEntity::class,
        EmergencyContactEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(StringListConverter::class)
abstract class MedMobDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun medicationDao(): MedicationDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun vitalSignsDao(): VitalSignsDao
    abstract fun emergencyContactDao(): EmergencyContactDao
    
    companion object {
        const val DATABASE_NAME = "medmob_patients_db"
    }
}
