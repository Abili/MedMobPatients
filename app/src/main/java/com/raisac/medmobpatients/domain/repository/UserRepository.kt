package com.raisac.medmobpatients.domain.repository

import com.raisac.medmobpatients.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user-related operations
 */
interface UserRepository {
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun getUserById(userId: String): Flow<User?>
    suspend fun deleteUser(userId: String): Result<Unit>
}
