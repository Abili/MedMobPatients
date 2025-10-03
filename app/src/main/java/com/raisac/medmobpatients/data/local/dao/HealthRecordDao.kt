package com.raisac.medmobpatients.data.local.dao

import androidx.room.*
import com.raisac.medmobpatients.data.local.entity.HealthRecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Health Records operations
 */
@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_records WHERE userId = :userId ORDER BY date DESC")
    fun getHealthRecordsByUserId(userId: String): Flow<List<HealthRecordEntity>>

    @Query("SELECT * FROM health_records WHERE id = :recordId")
    fun getHealthRecordById(recordId: String): Flow<HealthRecordEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(record: HealthRecordEntity)

    @Update
    suspend fun updateHealthRecord(record: HealthRecordEntity)

    @Delete
    suspend fun deleteHealthRecord(record: HealthRecordEntity)

    @Query("DELETE FROM health_records WHERE id = :recordId")
    suspend fun deleteHealthRecordById(recordId: String)
}
