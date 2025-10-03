package com.raisac.medmobpatients.domain.model

/**
 * Domain model for appointments
 */
data class Appointment(
    val id: String = "",
    val userId: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val doctorSpecialization: String = "",
    val appointmentDate: Long = System.currentTimeMillis(),
    val appointmentTime: String = "",
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED,
    val location: String = "",
    val notes: String = "",
    val reason: String = "",
    val isVideoConsultation: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AppointmentStatus {
    SCHEDULED,
    CONFIRMED,
    COMPLETED,
    CANCELLED,
    RESCHEDULED
}
