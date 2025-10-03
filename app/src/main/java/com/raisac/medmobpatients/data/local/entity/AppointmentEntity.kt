package com.raisac.medmobpatients.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for appointments
 */
@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val doctorId: String,
    val doctorName: String,
    val doctorSpecialization: String,
    val appointmentDate: Long,
    val appointmentTime: String,
    val status: String,
    val location: String,
    val notes: String,
    val reason: String,
    val isVideoConsultation: Boolean,
    val createdAt: Long
)
