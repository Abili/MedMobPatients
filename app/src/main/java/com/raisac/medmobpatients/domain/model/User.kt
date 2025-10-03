package com.raisac.medmobpatients.domain.model

/**
 * Domain model for User/Patient information
 */
data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val age: String = "",
    val profilePicUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getFullName(): String = "$firstName $lastName"
}
