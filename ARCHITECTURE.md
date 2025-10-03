# MedMobPatients - Modern Architecture Documentation

## Overview
MedMobPatients is a comprehensive healthcare companion application built with modern Android development practices, enabling patients to manage their health records, appointments, medications, and communicate with healthcare providers.

## Technology Stack

### Core Technologies
- **Language**: Kotlin 1.9.0
- **Minimum SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture
- **Dependency Injection**: Hilt
- **UI Framework**: Jetpack Compose with Material Design 3

### Key Libraries
- **Async**: Kotlin Coroutines & Flow
- **Database**: Room with encryption support
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil & Glide
- **Security**: 
  - Android Security Crypto (EncryptedSharedPreferences)
  - Tink for encryption
  - Biometric authentication
- **Firebase**: Auth, Firestore, Database, Storage, Messaging
- **Health Integration**: Health Connect API
- **Camera**: CameraX for document scanning
- **Maps**: Google Maps & Places API

## Project Structure

```
app/src/main/java/com/raisac/medmobpatients/
├── di/                          # Dependency Injection modules
│   └── FirebaseModule.kt        # Firebase dependencies
├── data/                        # Data layer
│   ├── local/                   # Room database & DAOs
│   ├── remote/                  # API services
│   └── repository/              # Repository implementations
├── domain/                      # Business logic layer
│   ├── model/                   # Domain models
│   │   ├── User.kt
│   │   ├── HealthRecord.kt
│   │   ├── Medication.kt
│   │   ├── Appointment.kt
│   │   ├── VitalSigns.kt
│   │   └── EmergencyContact.kt
│   ├── repository/              # Repository interfaces
│   │   ├── UserRepository.kt
│   │   └── HealthRecordRepository.kt
│   └── usecase/                 # Use cases
├── presentation/                # UI layer
│   ├── ui/                      # Compose UI screens
│   │   ├── components/          # Reusable UI components
│   │   └── theme/               # Material Design 3 theme
│   ├── viewmodel/               # ViewModels
│   └── navigation/              # Navigation graph
├── utils/                       # Utility classes
│   ├── SecureStorage.kt         # Encrypted storage
│   └── BiometricHelper.kt       # Biometric authentication
└── MedMobPatientsApplication.kt # Application class
```

## Architecture Principles

### MVVM Pattern
- **Model**: Domain models and data sources (Room, Firebase)
- **View**: Jetpack Compose UI components
- **ViewModel**: Business logic and state management

### Clean Architecture Layers
1. **Presentation Layer**: UI components, ViewModels
2. **Domain Layer**: Business logic, use cases, domain models
3. **Data Layer**: Repositories, data sources (local & remote)

### Dependency Flow
```
Presentation → Domain ← Data
```

## Key Features

### Security & Privacy
- ✅ End-to-end encryption for health data (AES-256)
- ✅ Biometric authentication (fingerprint/face)
- ✅ Secure storage with EncryptedSharedPreferences
- ✅ HIPAA/GDPR compliance ready

### Patient Management
- Personal health dashboard
- Medication reminders and tracking
- Symptom tracker
- Vital signs monitoring
- Medical document vault
- Emergency contacts

### Doctor Interaction
- Doctor search and filtering
- Appointment booking
- Secure messaging
- Video consultations (planned)
- Prescription management

### Health Monitoring
- Wearable device integration (Health Connect)
- Manual vital signs logging
- Health analytics and trends
- Nutrition and exercise tracking

## Development Setup

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34
- Gradle 8.0+

### Building the Project
```bash
./gradlew clean build
```

### Running Tests
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Security Considerations

### Data Protection
- All sensitive health data is encrypted at rest
- Network communications use HTTPS/TLS
- Firebase Security Rules for data access control
- Secure token management

### Authentication
- Firebase Authentication for user management
- Multi-factor authentication support
- Biometric authentication for quick access
- Secure session management

## Testing Strategy
- Unit tests for business logic and use cases
- Integration tests for repositories and data layer
- UI tests with Compose Testing
- Security testing for encryption and authentication

## Future Enhancements
- AI-powered health insights
- Advanced symptom checker
- Telemedicine integration
- Wearable device direct sync
- Offline-first architecture
- Multi-language support

## Contributing
This is a healthcare application. All contributions must maintain:
- Code quality and testing standards
- Security best practices
- Privacy compliance (HIPAA/GDPR)
- Accessibility requirements

## License
See LICENSE file for details.
