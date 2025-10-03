package com.raisac.medmobpatients.data.local.dao

import androidx.room.*
import com.raisac.medmobpatients.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Medications operations
 */
@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications WHERE userId = :userId AND isActive = 1 ORDER BY startDate DESC")
    fun getActiveMedicationsByUserId(userId: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE userId = :userId ORDER BY startDate DESC")
    fun getAllMedicationsByUserId(userId: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :medicationId")
    fun getMedicationById(medicationId: String): Flow<MedicationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)

    @Query("DELETE FROM medications WHERE id = :medicationId")
    suspend fun deleteMedicationById(medicationId: String)
}
