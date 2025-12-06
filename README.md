# ClassTrack App

ClassTrack is a scheduling application for students, built with modern Android development practices. It allows users to manage their classes, assignments, and reminders in a clean and intuitive interface.

# Screenshot of App
![](./Screenshot%202025-12-05%20at%204.42.56 PM.png)
# Figma Designs
![](./Screenshot%202025-12-05%20at%204.50.35 PM.png)
## Features

This application is made possible by a variety of Android and Jetpack Compose features:

*   **Jetpack Compose:** The entire UI is built using Jetpack Compose, including:
    *   **Material 3:** For modern, dynamic UI components.
    *   **Compose Navigation:** For navigating between screens in a type-safe manner using `kotlinx-serialization`.
    *   **State Management:** Utilizing `ViewModel`s and observing data streams (`StateFlow`) with `collectAsStateWithLifecycle` to create a reactive UI that automatically updates when data changes.

*   **MVVM Architecture:** The app follows the Model-View-ViewModel pattern to separate concerns, making the code more robust and maintainable.
    *   `ViewModel`s (`HomeScreenViewModel`, `CalendarViewModel`) handle UI logic.
    *   A `ViewModelFactory` is used for providing dependencies to the ViewModels.

*   **Room Database:** For local, persistent storage of all application data (classes, assignments, reminders).
    *   Data Access Objects (DAOs) provide an abstraction layer over the database.
    *   **Kotlin Flow:** Room returns data as `Flow`, allowing the UI to reactively update as the underlying data changes.

*   **Kotlin Coroutines:** Used to perform all database operations asynchronously, ensuring the main thread is never blocked.

*   **Third-Party Libraries:**
    *   **Kotlinx Serialization:** Used to enable type-safe navigation with Jetpack Compose, which is more robust than using string-based routes.
    *   **Gson:** Used to create a Room `TypeConverter` that allows `List<String>` objects to be stored in the database.

## Dependencies & Requirements

*   **Android SDK Version:** This app requires **Android SDK version 26 (Android 8.0 Oreo)** or higher. This is due to the use of the `java.time` API for handling dates and times, which was introduced in API level 26.
*   **Device Features:** No specific device hardware (like a camera or GPS) is required to run the app.

## Above and Beyond

Here are some areas where the project goes beyond the minimum requirements:

*   **Fully Reactive UI:** The Home and Calendar screens are fully reactive. They use `StateFlow` from the `ViewModel` to listen for changes in the Room database. This means that whenever a class, assignment, or reminder is added, updated, or deleted, the UI updates automatically without needing manual refreshes.

*   **Custom Room Type Converters:** The app uses a custom Room `TypeConverter` with the Gson library to seamlessly store a `List<String>` (for the days of the week a class occurs) in a single database column as a JSON string.

*   **Custom UI Styling:** Attention was given to UI/UX details, such as implementing a custom color for selected navigation items and removing the default background "indicator" to create a cleaner, more polished look and feel.
