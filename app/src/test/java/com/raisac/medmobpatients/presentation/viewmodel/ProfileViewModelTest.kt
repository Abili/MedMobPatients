package com.raisac.medmobpatients.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raisac.medmobpatients.domain.model.User
import com.raisac.medmobpatients.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Unit tests for ProfileViewModel
 */
@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    private lateinit var viewModel: ProfileViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val testUser = User(
        id = "test-user-123",
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
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        // Given
        `when`(getCurrentUserUseCase()).thenReturn(flowOf(testUser))

        // When
        viewModel = ProfileViewModel(getCurrentUserUseCase)

        // Then
        assertTrue(viewModel.uiState.value is ProfileUiState.Loading)
    }

    @Test
    fun `uiState updates to Success when user is loaded`() = runTest {
        // Given
        `when`(getCurrentUserUseCase()).thenReturn(flowOf(testUser))

        // When
        viewModel = ProfileViewModel(getCurrentUserUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ProfileUiState.Success)
        assertEquals(testUser, (state as ProfileUiState.Success).user)
    }

    @Test
    fun `uiState updates to Error when user is null`() = runTest {
        // Given
        `when`(getCurrentUserUseCase()).thenReturn(flowOf(null))

        // When
        viewModel = ProfileViewModel(getCurrentUserUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ProfileUiState.Error)
        assertEquals("User not found", (state as ProfileUiState.Error).message)
    }

    @Test
    fun `uiState updates to Error when exception occurs`() = runTest {
        // Given
        val errorMessage = "Network error"
        `when`(getCurrentUserUseCase()).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel = ProfileViewModel(getCurrentUserUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ProfileUiState.Error)
        assertEquals(errorMessage, (state as ProfileUiState.Error).message)
    }

    @Test
    fun `refresh reloads user data`() = runTest {
        // Given
        `when`(getCurrentUserUseCase()).thenReturn(flowOf(testUser))
        viewModel = ProfileViewModel(getCurrentUserUseCase)
        advanceUntilIdle()

        // When
        viewModel.refresh()
        advanceUntilIdle()

        // Then
        verify(getCurrentUserUseCase, times(2)).invoke()
    }
}
