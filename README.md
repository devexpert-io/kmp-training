# KMP Training App

A comprehensive note-taking application built with Kotlin Multiplatform, demonstrating modern app development practices across Android, iOS, and Desktop platforms.

## Contact

For questions, support, or training services, reach us at:
[https://devexpert.io/contacto](https://devexpert.io/contacto)

## Project Overview

This project showcases a complete Kotlin Multiplatform application with the following features:
- Google OAuth authentication
- Note management (create, read, update, delete)
- Cross-platform UI using Compose Multiplatform
- Offline support with SQLDelight
- REST API integration with Ktor

## Architecture

The project is structured into three main modules:

### 1. composeApp
Contains the UI implementation using Compose Multiplatform, including:
- Screens (Login, Notes list, Note detail)
- ViewModels
- UI components
- Theme and resources

### 2. shared
Contains the common business logic and data layer:
- Domain models
- Repositories
- Data sources
- DI configuration
- Platform-specific implementations

### 3. server
A Ktor-based backend implementation providing:
- REST API endpoints
- Google OAuth integration
- Database operations
- JWT authentication

## Main Technologies

- **UI Framework**: Compose Multiplatform
- **Dependency Injection**: Koin
- **Local Storage**: SQLDelight
- **Networking**: Ktor Client/Server
- **Authentication**: Google OAuth + JWT
- **Image Loading**: Coil
- **State Management**: Kotlin Flow
- **Build System**: Gradle with Version Catalogs

## Setup

1. Clone the repository
2. Set up environment variables for Google OAuth:
   ```
   GOOGLE_CLIENT_ID=your_client_id
   GOOGLE_CLIENT_SECRET=your_client_secret
   JWT_SECRET=your_jwt_secret
   ```
3. Run the server:
   ```
   ./gradlew :server:run
   ```
4. Run the application:
   - Android: Open in Android Studio and run
   - iOS: Open `iosApp/iosApp.xcodeproj` and run
   - Desktop: `./gradlew :composeApp:run`

## Project Structure

├── composeApp/ # Main UI module
│ ├── commonMain/ # Cross-platform UI code
│ ├── androidMain/ # Android-specific code
│ ├── iosMain/ # iOS-specific code
│ └── desktopMain/ # Desktop-specific code
├── shared/ # Common business logic
│ ├── commonMain/ # Cross-platform code
│ ├── androidMain/ # Android-specific implementations
│ ├── iosMain/ # iOS-specific implementations
│ └── jvmMain/ # Desktop-specific implementations
└── server/ # Backend implementation



## License

This project is part of DevExpert's Kotlin Multiplatform training materials.