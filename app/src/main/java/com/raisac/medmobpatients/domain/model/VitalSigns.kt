package com.raisac.medmobpatients.domain.model

/**
 * Domain model for vital signs tracking
 */
data class VitalSigns(
    val id: String = "",
    val userId: String = "",
    val bloodPressureSystolic: Int? = null,
    val bloodPressureDiastolic: Int? = null,
    val heartRate: Int? = null,
    val temperature: Double? = null,
    val oxygenSaturation: Int? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val glucoseLevel: Int? = null,
    val notes: String = "",
    val recordedAt: Long = System.currentTimeMillis()
) {
    fun getBMI(): Double? {
        return if (weight != null && height != null && height > 0) {
            val heightInMeters = height / 100
            weight / (heightInMeters * heightInMeters)
        } else null
    }
}
