# Developer Integration Guide

## Overview
This guide helps developers understand how to work with the modernized MedMobPatients codebase.

## Architecture Layers

### 1. Domain Layer (Business Logic)
The domain layer contains pure business logic with no Android dependencies.

#### Creating a Domain Model
```kotlin
data class YourModel(
    val id: String = "",
    val name: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
```

#### Creating a Repository Interface
```kotlin
interface YourRepository {
    suspend fun getData(id: String): Flow<YourModel?>
    suspend fun saveData(data: YourModel): Result<Unit>
}
```

#### Creating a Use Case
```kotlin
class GetDataUseCase @Inject constructor(
    private val repository: YourRepository
) {
    suspend operator fun invoke(id: String): Flow<YourModel?> {
        return repository.getData(id)
    }
}
```

### 2. Data Layer (Data Management)

#### Creating a Room Entity
```kotlin
@Entity(tableName = "your_table")
data class YourEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val createdAt: Long
)
```

#### Creating a DAO
```kotlin
@Dao
interface YourDao {
    @Query("SELECT * FROM your_table WHERE id = :id")
    fun getById(id: String): Flow<YourEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: YourEntity)
}
```

#### Creating Data Mappers
```kotlin
fun YourEntity.toDomain(): YourModel {
    return YourModel(id = id, name = name, createdAt = createdAt)
}

fun YourModel.toEntity(): YourEntity {
    return YourEntity(id = id, name = name, createdAt = createdAt)
}
```

#### Implementing Repository
```kotlin
class YourRepositoryImpl @Inject constructor(
    private val dao: YourDao
) : YourRepository {
    override suspend fun getData(id: String): Flow<YourModel?> {
        return dao.getById(id).map { it?.toDomain() }
    }
    
    override suspend fun saveData(data: YourModel): Result<Unit> {
        return try {
            dao.insert(data.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 3. Presentation Layer (UI)

#### Creating a ViewModel
```kotlin
@HiltViewModel
class YourViewModel @Inject constructor(
    private val getDataUseCase: GetDataUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadData(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                getDataUseCase(id).collect { data ->
                    _uiState.value = if (data != null) {
                        UiState.Success(data)
                    } else {
                        UiState.Error("Not found")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: YourModel) : UiState()
    data class Error(val message: String) : UiState()
}
```

#### Creating a Compose Screen
```kotlin
@Composable
fun YourScreen(
    viewModel: YourViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Screen") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = uiState) {
                is UiState.Loading -> LoadingIndicator()
                is UiState.Success -> YourContent(state.data)
                is UiState.Error -> ErrorMessage(state.message)
            }
        }
    }
}
```

## Dependency Injection with Hilt

### Adding to Database
1. Add entity to `MedMobDatabase.kt`
2. Create DAO provider in `DatabaseModule.kt`

### Adding Repository
1. Create implementation in `data/repository`
2. Bind in `RepositoryModule.kt`:
```kotlin
@Binds
@Singleton
abstract fun bindYourRepository(
    impl: YourRepositoryImpl
): YourRepository
```

## Testing

### Unit Test Example
```kotlin
class YourRepositoryTest {
    @Mock
    private lateinit var dao: YourDao
    
    private lateinit var repository: YourRepositoryImpl
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = YourRepositoryImpl(dao)
    }
    
    @Test
    fun `test getData returns expected result`() = runTest {
        // Given
        val entity = YourEntity(id = "1", name = "Test", createdAt = 0L)
        `when`(dao.getById("1")).thenReturn(flowOf(entity))
        
        // When
        val result = repository.getData("1").first()
        
        // Then
        assertNotNull(result)
        assertEquals("Test", result?.name)
    }
}
```

## Best Practices

### 1. State Management
- Use `StateFlow` for UI state in ViewModels
- Always handle Loading, Success, and Error states
- Use sealed classes for UI state

### 2. Coroutines
- Use `viewModelScope` for ViewModel operations
- Use `Flow` for reactive data streams
- Handle exceptions properly with try-catch

### 3. Compose UI
- Keep composables small and focused
- Use `remember` for expensive operations
- Hoist state to the ViewModel
- Use `LaunchedEffect` for side effects

### 4. Security
- Use `SecureStorage` for sensitive data
- Never log sensitive information
- Validate all user inputs
- Use biometric auth for sensitive operations

### 5. Database
- Always use transactions for multiple operations
- Use Flow for reactive queries
- Create indexes for frequently queried fields
- Handle migrations properly

## Common Patterns

### Loading Data on Screen Load
```kotlin
LaunchedEffect(Unit) {
    viewModel.loadData()
}
```

### Form Validation
```kotlin
var name by remember { mutableStateOf("") }
var isError by remember { mutableStateOf(false) }

AppTextField(
    value = name,
    onValueChange = { 
        name = it
        isError = it.isEmpty()
    },
    label = "Name",
    isError = isError,
    errorMessage = "Name is required"
)
```

### Navigation
```kotlin
// Setup navigation in your Activity
NavHost(navController, startDestination = "welcome") {
    composable("welcome") { WelcomeScreen() }
    composable("profile") { ProfileScreen() }
}

// Navigate
navController.navigate("profile")
```

## Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Hilt Documentation](https://dagger.dev/hilt/)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Material Design 3](https://m3.material.io/)

## Getting Help

1. Check the ARCHITECTURE.md file for overall structure
2. Look at existing implementations for patterns
3. Review tests for usage examples
4. Open an issue on GitHub for questions
