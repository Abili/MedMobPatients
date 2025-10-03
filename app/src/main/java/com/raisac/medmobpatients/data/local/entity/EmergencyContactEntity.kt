package com.raisac.medmobpatients.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for emergency contacts
 */
@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val email: String,
    val isPrimary: Boolean,
    val createdAt: Long
)
