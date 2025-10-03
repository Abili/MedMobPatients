# MedMobPatients - Modern Healthcare Companion App

A comprehensive, patient-centric healthcare companion application built with modern Android development practices.

## 🏥 Overview

MedMobPatients is a top-tier healthcare application that empowers patients to take control of their health journey. Built with Kotlin, Jetpack Compose, and following clean architecture principles, it provides a seamless experience for managing health records, appointments, medications, and communicating with healthcare providers.

## ✨ Key Features

### 🔐 Security & Privacy
- **Biometric Authentication**: Fingerprint and face recognition support
- **End-to-End Encryption**: AES-256 encryption for sensitive health data
- **Secure Storage**: EncryptedSharedPreferences for local data protection
- **HIPAA/GDPR Ready**: Compliance framework built-in

### 📱 Core Features
- **Health Dashboard**: Personalized dashboard with quick access to key features
- **Medication Management**: Track medications with smart reminders
- **Appointment Booking**: Schedule and manage doctor appointments
- **Health Records**: Digital vault for medical documents and records
- **Vital Signs Tracking**: Monitor blood pressure, heart rate, weight, and more
- **Emergency Contacts**: Quick access to emergency contacts
- **Doctor Communication**: Secure messaging with healthcare providers

### 🎨 Modern UI/UX
- **Material Design 3**: Beautiful, accessible interface
- **Dark Theme Support**: System-adaptive dark and light themes
- **Jetpack Compose**: Modern, declarative UI framework
- **Responsive Design**: Optimized for all screen sizes

## 🏗️ Technology Stack

### Core Technologies
- **Language**: Kotlin 1.8.0
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)

### Key Libraries
- **Async**: Kotlin Coroutines & Flow
- **Database**: Room with encryption
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil & Glide
- **Firebase**: Auth, Firestore, Database, Storage
- **Security**: Tink, Security Crypto, Biometric API
- **Health**: Health Connect API
- **Camera**: CameraX for document scanning

## 📂 Project Structure

```
app/src/main/java/com/raisac/medmobpatients/
├── di/                          # Dependency Injection
│   ├── DatabaseModule.kt
│   ├── FirebaseModule.kt
│   └── RepositoryModule.kt
├── data/                        # Data Layer
│   ├── local/                   # Room Database
│   │   ├── entity/             # Database Entities
│   │   ├── dao/                # Data Access Objects
│   │   └── converter/          # Type Converters
│   ├── remote/                 # API Services
│   ├── repository/             # Repository Implementations
│   └── mapper/                 # Entity-Domain Mappers
├── domain/                     # Domain Layer
│   ├── model/                  # Domain Models
│   ├── repository/             # Repository Interfaces
│   └── usecase/                # Business Logic Use Cases
├── presentation/               # Presentation Layer
│   ├── ui/
│   │   ├── screens/           # Compose Screens
│   │   ├── components/        # Reusable Components
│   │   └── theme/             # Material Design Theme
│   ├── viewmodel/             # ViewModels
│   └── navigation/            # Navigation
├── utils/                      # Utilities
│   ├── SecureStorage.kt
│   └── BiometricHelper.kt
└── MedMobPatientsApplication.kt
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34
- Gradle 8.0+

### Building the Project

1. Clone the repository:
```bash
git clone https://github.com/Abili/MedMobPatients.git
cd MedMobPatients
```

2. Open in Android Studio and sync Gradle

3. Add your Firebase configuration:
   - Download `google-services.json` from Firebase Console
   - Place it in `app/` directory

4. Build and run:
```bash
./gradlew clean build
```

## 🗄️ Database Schema

The app uses Room database with the following entities:
- **Users**: Patient information
- **HealthRecords**: Medical documents and records
- **Medications**: Medication tracking with reminders
- **Appointments**: Doctor appointments
- **VitalSigns**: Health monitoring data
- **EmergencyContacts**: Emergency contact information

## 🔒 Security Features

- **Encrypted Storage**: All sensitive data encrypted at rest
- **Secure Communication**: HTTPS/TLS for network calls
- **Biometric Auth**: Optional fingerprint/face authentication
- **Firebase Security Rules**: Server-side data protection
- **No Plain Text Secrets**: Secure credential management

## 🧪 Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

## 📱 Screenshots

*Coming soon - Modern Compose UI screenshots*

## 🤝 Contributing

This is a healthcare application. All contributions must maintain:
- Code quality standards
- Security best practices
- Privacy compliance (HIPAA/GDPR)
- Accessibility requirements

## 📄 License

See LICENSE file for details.

## 📞 Support

For support and questions, please open an issue on GitHub.

---

**Note**: This is a modernized version of the original MedMobPatients app, rebuilt with Kotlin, Jetpack Compose, and modern Android architecture patterns.
