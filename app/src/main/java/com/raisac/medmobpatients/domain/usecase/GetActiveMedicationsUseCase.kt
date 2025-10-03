package com.raisac.medmobpatients.domain.usecase

import com.raisac.medmobpatients.domain.model.Medication
import com.raisac.medmobpatients.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get active medications for a user
 */
class GetActiveMedicationsUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) {
    suspend operator fun invoke(userId: String): Flow<List<Medication>> {
        return medicationRepository.getActiveMedications(userId)
    }
}
