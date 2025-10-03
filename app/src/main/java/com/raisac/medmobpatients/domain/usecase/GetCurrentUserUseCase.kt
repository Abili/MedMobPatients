package com.raisac.medmobpatients.domain.usecase

import com.raisac.medmobpatients.domain.model.User
import com.raisac.medmobpatients.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get current user information
 */
class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<User?> {
        return userRepository.getCurrentUser()
    }
}
