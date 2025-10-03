package com.raisac.medmobpatients.domain.model

/**
 * Domain model for medication and reminders
 */
data class Medication(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val dosage: String = "",
    val frequency: String = "",
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long? = null,
    val reminderTimes: List<String> = emptyList(),
    val notes: String = "",
    val prescriptionUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
