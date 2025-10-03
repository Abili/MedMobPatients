package com.raisac.medmobpatients.data.mapper

import com.raisac.medmobpatients.data.local.entity.UserEntity
import com.raisac.medmobpatients.domain.model.User

/**
 * Extension functions to map between UserEntity and User domain model
 */
fun UserEntity.toDomain(): User {
    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        dateOfBirth = dateOfBirth,
        age = age,
        profilePicUrl = profilePicUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        dateOfBirth = dateOfBirth,
        age = age,
        profilePicUrl = profilePicUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
