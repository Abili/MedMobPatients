package com.raisac.medmobpatients.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for User/Patient information
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val age: String,
    val profilePicUrl: String,
    val createdAt: Long,
    val updatedAt: Long
)
