# Currency Converter Application

## Overview

This Android application allows users to fetch and display real-time currency exchange rates and perform conversions based on a user-selected primary currency. The app is built using **MVVM** architecture with **Clean Code principles** for scalability and maintainability. The project integrates modern Android development libraries and tools, including **Jetpack Compose**, **Coroutines**, **Flow**, **Hilt**, and **Room Database**.

---

## Features

- Fetches real-time currency rates every 3 seconds.
- Allows the user to:
    - Select a primary currency.
    - Input an amount for the primary currency.
    - View conversion rates for other currencies relative to the primary currency.
- Follows **MVVM** and **Clean Architecture** principles.
- Implements robust unit testing for use cases, repositories, and the `ViewModel`.

---

## Architecture

The project adheres to the **Clean Architecture** structure, ensuring separation of concerns through three layers:

1. **Data Layer**
    - Handles data sources (e.g., network and database).
    - Provides implementation for repositories.
    - Example Components:
        - `Room Database` for offline data storage.
        - Retrofit/other APIs (if used) for network calls.

2. **Domain Layer**
    - Contains business logic in the form of **Use Cases**.
    - Use Cases:
        - `AddCurrencyRatesUseCase`: Adds/updates currency rates in the local database.
        - `GetCurrencyRatesUseCase`: Retrieves currency rates for the selected primary currency.
    - Located in the `com.example.mintosassignment.domain.usecases` package.

3. **Presentation Layer**
    - Manages UI using **Jetpack Compose**.
    - Utilizes **StateFlow** to observe changes from the `ViewModel`.
    - Example Component:
        - `CurrencyViewModel`: Responsible for UI-related logic, located in `com.example.mintosassignment.data.view_models`.

---

## Tech Stack

- **Kotlin**: Primary programming language.
- **Jetpack Compose**: For building a modern UI.
- **MVVM Architecture**: Ensures separation of concerns and testability.
- **Coroutines & Flow**: Handles asynchronous operations and state management.
- **Hilt**: For dependency injection.
- **Room Database**: Local storage for offline support.

---


## Unit Testing

The project includes comprehensive **unit tests** for:

1. **Use Cases**:
    - Testing business logic to ensure accurate functionality.
2. **Repositories**:
    - Validating data access and persistence logic.
3. **ViewModel**:
    - Verifying UI-related logic.

Testing tools used:
- JUnit for writing unit tests.
- MockK/Mockito for mocking dependencies.

---
![Screen_recording_20241126_102417-ezgif com-effects](https://github.com/user-attachments/assets/737c9684-f0d1-4fea-8c32-73161ba51319)
