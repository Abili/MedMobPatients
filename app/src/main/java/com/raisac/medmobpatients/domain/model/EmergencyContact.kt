package com.raisac.medmobpatients.domain.model

/**
 * Domain model for emergency contacts
 */
data class EmergencyContact(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val relationship: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val isPrimary: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
