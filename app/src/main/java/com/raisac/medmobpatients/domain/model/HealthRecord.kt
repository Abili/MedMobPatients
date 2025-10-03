package com.raisac.medmobpatients.domain.model

/**
 * Domain model for health records
 */
data class HealthRecord(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val recordType: RecordType = RecordType.GENERAL,
    val date: Long = System.currentTimeMillis(),
    val documentUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class RecordType {
    GENERAL,
    LAB_RESULT,
    PRESCRIPTION,
    DIAGNOSIS,
    VACCINATION,
    IMAGING,
    SURGERY,
    ALLERGY
}
