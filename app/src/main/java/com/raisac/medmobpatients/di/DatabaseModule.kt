package com.raisac.medmobpatients.di

import android.content.Context
import androidx.room.Room
import com.raisac.medmobpatients.data.local.MedMobDatabase
import com.raisac.medmobpatients.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing Room database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MedMobDatabase {
        return Room.databaseBuilder(
            context,
            MedMobDatabase::class.java,
            MedMobDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: MedMobDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideHealthRecordDao(database: MedMobDatabase): HealthRecordDao {
        return database.healthRecordDao()
    }

    @Provides
    @Singleton
    fun provideMedicationDao(database: MedMobDatabase): MedicationDao {
        return database.medicationDao()
    }

    @Provides
    @Singleton
    fun provideAppointmentDao(database: MedMobDatabase): AppointmentDao {
        return database.appointmentDao()
    }

    @Provides
    @Singleton
    fun provideVitalSignsDao(database: MedMobDatabase): VitalSignsDao {
        return database.vitalSignsDao()
    }

    @Provides
    @Singleton
    fun provideEmergencyContactDao(database: MedMobDatabase): EmergencyContactDao {
        return database.emergencyContactDao()
    }
}
