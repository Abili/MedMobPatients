package com.raisac.medmobpatients.domain.repository

import com.raisac.medmobpatients.domain.model.HealthRecord
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for health records operations
 */
interface HealthRecordRepository {
    suspend fun getHealthRecords(userId: String): Flow<List<HealthRecord>>
    suspend fun getHealthRecordById(recordId: String): Flow<HealthRecord?>
    suspend fun addHealthRecord(record: HealthRecord): Result<Unit>
    suspend fun updateHealthRecord(record: HealthRecord): Result<Unit>
    suspend fun deleteHealthRecord(recordId: String): Result<Unit>
}
