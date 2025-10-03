package com.raisac.medmobpatients.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.raisac.medmobpatients.data.local.dao.UserDao
import com.raisac.medmobpatients.data.local.entity.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Unit tests for UserRepositoryImpl
 */
class UserRepositoryImplTest {

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var repository: UserRepositoryImpl

    private val testUserId = "test-user-123"
    private val testUserEntity = UserEntity(
        id = testUserId,
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        phoneNumber = "+1234567890",
        dateOfBirth = "1990-01-01",
        age = "34",
        profilePicUrl = "https://example.com/pic.jpg",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = UserRepositoryImpl(userDao, firebaseAuth)
    }

    @Test
    fun `getCurrentUser returns user when authenticated`() = runTest {
        // Given
        `when`(firebaseAuth.currentUser).thenReturn(firebaseUser)
        `when`(firebaseUser.uid).thenReturn(testUserId)
        `when`(userDao.getUserById(testUserId)).thenReturn(flowOf(testUserEntity))

        // When
        val result = repository.getCurrentUser().first()

        // Then
        assertNotNull(result)
        assertEquals(testUserId, result?.id)
        assertEquals("John", result?.firstName)
        assertEquals("Doe", result?.lastName)
        assertEquals("john.doe@example.com", result?.email)
    }

    @Test
    fun `getCurrentUser returns null when not authenticated`() = runTest {
        // Given
        `when`(firebaseAuth.currentUser).thenReturn(null)

        // When
        val result = repository.getCurrentUser().first()

        // Then
        assertNull(result)
        verify(userDao, never()).getUserById(anyString())
    }

    @Test
    fun `getUserById returns user successfully`() = runTest {
        // Given
        `when`(userDao.getUserById(testUserId)).thenReturn(flowOf(testUserEntity))

        // When
        val result = repository.getUserById(testUserId).first()

        // Then
        assertNotNull(result)
        assertEquals(testUserId, result?.id)
        verify(userDao).getUserById(testUserId)
    }

    @Test
    fun `updateUser calls dao update`() = runTest {
        // Given
        val user = testUserEntity.let {
            com.raisac.medmobpatients.domain.model.User(
                id = it.id,
                firstName = it.firstName,
                lastName = it.lastName,
                email = it.email,
                phoneNumber = it.phoneNumber,
                dateOfBirth = it.dateOfBirth,
                age = it.age,
                profilePicUrl = it.profilePicUrl,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }

        // When
        val result = repository.updateUser(user)

        // Then
        assertTrue(result.isSuccess)
        verify(userDao).updateUser(any())
    }

    @Test
    fun `deleteUser calls dao delete`() = runTest {
        // When
        val result = repository.deleteUser(testUserId)

        // Then
        assertTrue(result.isSuccess)
        verify(userDao).deleteUserById(testUserId)
    }
}
