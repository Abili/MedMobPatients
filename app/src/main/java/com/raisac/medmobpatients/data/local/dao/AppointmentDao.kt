package com.raisac.medmobpatients.data.local.dao

import androidx.room.*
import com.raisac.medmobpatients.data.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Appointments operations
 */
@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY appointmentDate DESC")
    fun getAppointmentsByUserId(userId: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE id = :appointmentId")
    fun getAppointmentById(appointmentId: String): Flow<AppointmentEntity?>

    @Query("SELECT * FROM appointments WHERE userId = :userId AND appointmentDate >= :currentTime ORDER BY appointmentDate ASC")
    fun getUpcomingAppointments(userId: String, currentTime: Long): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Delete
    suspend fun deleteAppointment(appointment: AppointmentEntity)

    @Query("DELETE FROM appointments WHERE id = :appointmentId")
    suspend fun deleteAppointmentById(appointmentId: String)
}
