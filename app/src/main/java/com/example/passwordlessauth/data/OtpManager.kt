package com.example.passwordlessauth.data

import timber.log.Timber
import kotlin.random.Random

class OtpManager {

    // Stores OTP per email
    private val otpStore: MutableMap<String, OtpData> = mutableMapOf()

    private val otpExpiryMillis = 60_000L // 60 seconds

    /**
     * Generates a new OTP.
     * Old OTP (if any) is invalidated automatically.
     */
    fun generateOtp(email: String): String {
        val otp = Random.nextInt(100000, 999999).toString()

        otpStore[email] = OtpData(
            code = otp,
            generatedAt = System.currentTimeMillis()
        )
        Timber.d("OTP for $email is $otp") // 👈 TEMPORARY
        return otp
    }

    /**
     * Validates the user-entered OTP.
     */
    fun validateOtp(email: String, inputOtp: String): OtpResult {
        val data = otpStore[email] ?: return OtpResult.NotFound

        // 1️⃣ Check expiry
        val isExpired =
            System.currentTimeMillis() - data.generatedAt > otpExpiryMillis
        if (isExpired) {
            otpStore.remove(email)
            return OtpResult.Expired
        }

        // 2️⃣ Check attempts
        if (data.attemptsUsed >= data.maxAttempts) {
            otpStore.remove(email)
            return OtpResult.AttemptsExceeded
        }

        // 3️⃣ Check OTP match
        if (data.code == inputOtp) {
            otpStore.remove(email)
            return OtpResult.Success
        }

        // 4️⃣ Wrong OTP → increment attempts
        otpStore[email] = data.copy(
            attemptsUsed = data.attemptsUsed + 1
        )

        return OtpResult.Invalid
    }

    fun getRemainingAttempts(email: String): Int {
        val data = otpStore[email] ?: return 0
        return (data.maxAttempts - data.attemptsUsed).coerceAtLeast(0)
    }

    fun getExpiry(email: String): Long {
        return otpStore[email]?.generatedAt?.plus(60_000L) ?: 0L
    }

    /**
     * Explicit cleanup (used on logout or resend).
     */
    fun clearOtp(email: String) {
        otpStore.remove(email)
    }
}
