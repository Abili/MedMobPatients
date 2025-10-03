package com.raisac.medmobpatients.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.raisac.medmobpatients.data.local.dao.UserDao
import com.raisac.medmobpatients.data.mapper.toDomain
import com.raisac.medmobpatients.data.mapper.toEntity
import com.raisac.medmobpatients.domain.model.User
import com.raisac.medmobpatients.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of UserRepository
 * Handles user data from both local database and Firebase
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth
) : UserRepository {

    override suspend fun getCurrentUser(): Flow<User?> {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return kotlinx.coroutines.flow.flowOf(null)
        return userDao.getUserById(currentUserId).map { it?.toDomain() }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            userDao.updateUser(user.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: String): Flow<User?> {
        return userDao.getUserById(userId).map { it?.toDomain() }
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            userDao.deleteUserById(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
