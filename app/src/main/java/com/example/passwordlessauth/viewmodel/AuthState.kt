package com.example.passwordlessauth.viewmodel

/**
 * Represents all possible UI states for authentication.
 */
sealed class AuthState {

    object EnterEmail : AuthState()

    data class EnterOtp(
        val email: String,
        val remainingAttempts: Int,
        val expiresAt: Long,
        val errorMessage: String? = null
    ) : AuthState()

    data class LoggedIn(
        val email: String,
        val sessionStartTime: Long
    ) : AuthState()

    data class Error(val message: String) : AuthState()
}

