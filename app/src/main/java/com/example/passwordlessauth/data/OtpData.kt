package com.example.passwordlessauth.data

/**
 * Represents OTP state for a single email.
 * Pure data holder — no logic here.
 */
data class OtpData(
    val code: String,
    val generatedAt: Long,
    val attemptsUsed: Int = 0,
    val maxAttempts: Int = 3
)
