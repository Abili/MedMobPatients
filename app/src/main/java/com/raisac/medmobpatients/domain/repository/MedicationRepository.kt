package com.raisac.medmobpatients.domain.repository

import com.raisac.medmobpatients.domain.model.Medication
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for medication operations
 */
interface MedicationRepository {
    suspend fun getActiveMedications(userId: String): Flow<List<Medication>>
    suspend fun getAllMedications(userId: String): Flow<List<Medication>>
    suspend fun getMedicationById(medicationId: String): Flow<Medication?>
    suspend fun addMedication(medication: Medication): Result<Unit>
    suspend fun updateMedication(medication: Medication): Result<Unit>
    suspend fun deleteMedication(medicationId: String): Result<Unit>
}
