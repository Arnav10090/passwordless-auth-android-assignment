# Passwordless Authentication App (Android)

This project implements a **passwordless authentication flow** using **Email + One-Time Password (OTP)**, built with **Kotlin** and **Jetpack Compose**.

The app is fully local (no backend) and demonstrates clean architecture, proper state management, and modern Android development practices.

---

## 📱 Features

- Email-based login with locally generated OTP
- 6-digit OTP with:
    - 60-second expiry
    - Maximum 3 validation attempts
- Resend OTP functionality (invalidates previous OTP)
- Session screen with:
    - Session start time
    - Live session duration timer (mm:ss)
- Logout functionality
- Event logging using Timber

---

## 🏗 Architecture Overview

The app follows a **clean MVVM architecture** with unidirectional data flow:

```
UI (Compose) ↓ events ViewModel ↓ state UI State (StateFlow)
```

### Package Structure

```
com.example.passwordlessauth
│
├── data
│   ├── OtpData.kt
│   ├── OtpManager.kt
│   └── OtpResult.kt
│
├── viewmodel
│   └── AuthViewModel.kt
│
├── presentation
│   ├── AuthRoot.kt
│   ├── LoginScreen.kt
│   ├── OtpScreen.kt
│   ├── SessionScreen.kt
│   └── ErrorScreen.kt
│
├── ui.theme
│   └── Theme / Color / Typography files
│
├── App.kt
└── MainActivity.kt
```

### Design Principles

- UI layer is **stateless**
- ViewModel contains **all business logic**
- No global mutable state
- One-way data flow using `StateFlow`

---

## 🔐 OTP Logic Explained

OTP handling is implemented entirely locally for simplicity.

### OTP Rules
- Length: **6 digits**
- Expiry: **60 seconds**
- Maximum attempts: **3**
- OTPs are stored **per email**

### Data Model

Each OTP is represented by:

- Generated code
- Generation timestamp
- Attempts used
- Maximum allowed attempts

Attempts are tracked using `attemptsUsed` instead of `attemptsLeft` to:
- Avoid negative state
- Simplify validation logic

### Validation Outcomes

OTP validation returns a sealed result:
- Success
- Invalid
- Expired
- AttemptsExceeded
- NotFound

These results are mapped to UI state in the ViewModel.

---

## ❗ Error Handling Strategy

Errors are categorized into two types:

### Recoverable Errors
- Incorrect OTP (attempts remaining)

User stays on OTP screen and can retry.

### Terminal Errors
- OTP expired
- Maximum attempts exceeded
- OTP not found

User is redirected to an error screen and must restart the flow.

This ensures:
- Correct attempt tracking
- Predictable state transitions
- Clear user feedback

---

## ⏱ Session Management

- Session starts on successful OTP validation
- Session start time is stored in ViewModel
- A live session duration timer is displayed
- Timer survives recompositions
- Logout clears session state and returns to login

---

## 📊 Analytics / Logging

**Timber** is used for logging (debug builds only).

Logged events:
- OTP generated
- OTP validation success
- OTP validation failure
- Logout

A small `AnalyticsLogger` wrapper is used to:
- Keep SDK usage out of UI and ViewModel
- Allow easy replacement with Firebase or Sentry later

---

## 🧪 Testing & Edge Cases

Manually tested scenarios:
- OTP expiry
- Incorrect OTP attempts
- Maximum attempts exceeded
- Resend OTP invalidates previous OTP
- Screen rotation / recomposition
- Logout resets session correctly

---

## ⚠ Known Limitations

- OTPs are not persisted across app restarts
- No real email delivery (OTP is generated locally)
- Session does not survive process death

These trade-offs were intentional to match the assignment scope.

---

## 🚀 Setup Instructions

### Prerequisites
- Android Studio (latest stable)
- Android SDK installed
- Emulator or physical Android device

### Steps to Run

1. Clone the repository:
```
git clone https://github.com/<your-username>/passwordless-auth-android.git
```

2. Open Android Studio → **Open** → select the cloned folder

3. Let Gradle sync complete

4. Run the app on an emulator or physical device

No additional configuration is required.

---

## 🤖 AI Usage Disclosure

AI tools were used as a learning aid to:
- Clarify Jetpack Compose patterns
- Validate architectural decisions
- Debug specific implementation issues

All code was written, understood, and tested by me.
No code was blindly copied

---

## ✅ Assignment Checklist

- [x] Kotlin
- [x] Jetpack Compose
- [x] MVVM architecture
- [x] Coroutines / StateFlow
- [x] OTP rules enforced
- [x] Session tracking
- [x] External SDK integration
- [x] Clean, buildable project