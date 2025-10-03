package com.raisac.medmobpatients.data.local.dao

import androidx.room.*
import com.raisac.medmobpatients.data.local.entity.VitalSignsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Vital Signs operations
 */
@Dao
interface VitalSignsDao {
    @Query("SELECT * FROM vital_signs WHERE userId = :userId ORDER BY recordedAt DESC")
    fun getVitalSignsByUserId(userId: String): Flow<List<VitalSignsEntity>>

    @Query("SELECT * FROM vital_signs WHERE id = :vitalSignsId")
    fun getVitalSignsById(vitalSignsId: String): Flow<VitalSignsEntity?>

    @Query("SELECT * FROM vital_signs WHERE userId = :userId AND recordedAt >= :startDate AND recordedAt <= :endDate ORDER BY recordedAt DESC")
    fun getVitalSignsByDateRange(userId: String, startDate: Long, endDate: Long): Flow<List<VitalSignsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVitalSigns(vitalSigns: VitalSignsEntity)

    @Update
    suspend fun updateVitalSigns(vitalSigns: VitalSignsEntity)

    @Delete
    suspend fun deleteVitalSigns(vitalSigns: VitalSignsEntity)

    @Query("DELETE FROM vital_signs WHERE id = :vitalSignsId")
    suspend fun deleteVitalSignsById(vitalSignsId: String)
}
