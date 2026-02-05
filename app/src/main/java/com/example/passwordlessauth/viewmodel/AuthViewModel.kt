package com.example.passwordlessauth.viewmodel

import androidx.lifecycle.ViewModel
import com.example.passwordlessauth.data.AnalyticsLogger
import com.example.passwordlessauth.data.OtpManager
import com.example.passwordlessauth.data.OtpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    private val otpManager: OtpManager = OtpManager()
) : ViewModel() {

    private val _authState =
        MutableStateFlow<AuthState>(AuthState.EnterEmail)

    val authState: StateFlow<AuthState> =
        _authState.asStateFlow()

    /**
     * Called when user taps "Send OTP"
     */

    private val logger = AnalyticsLogger()
    fun sendOtp(email: String) {
        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Invalid email address")
            return
        }

        otpManager.generateOtp(email)
        logger.logOtpGenerated(email)

        _authState.value = AuthState.EnterOtp(
            email = email,
            remainingAttempts = 3,
            expiresAt = System.currentTimeMillis() + 60_000
        )
    }

    /**
     * Called when user submits OTP
     */
    fun verifyOtp(email: String, otp: String) {
        when (val result = otpManager.validateOtp(email, otp)) {

            OtpResult.Success -> {
                logger.logOtpSuccess(email)

                _authState.value = AuthState.LoggedIn(
                    email = email,
                    sessionStartTime = System.currentTimeMillis()
                )
            }

            OtpResult.Invalid -> {
                // 🔁 Recoverable error → STAY on OTP screen
                logger.logOtpFailure(email, "Invalid OTP")

                _authState.value = AuthState.EnterOtp(
                    email = email,
                    remainingAttempts = otpManager.getRemainingAttempts(email),
                    expiresAt = otpManager.getExpiry(email),
                    errorMessage = "Incorrect OTP"
                )
            }

            OtpResult.Expired -> {
                // 🛑 Terminal error → go to ErrorScreen
                logger.logOtpFailure(email, "Expired OTP")

                _authState.value =
                    AuthState.Error("OTP expired. Please request a new OTP.")
            }

            OtpResult.AttemptsExceeded -> {
                // 🛑 Terminal error → go to ErrorScreen
                logger.logOtpFailure(email, "Attempts exceeded")

                _authState.value =
                    AuthState.Error("Maximum attempts exceeded.")
            }

            OtpResult.NotFound -> {
                // 🛑 Terminal error → go to ErrorScreen
                logger.logOtpFailure(email, "OTP not found")

                _authState.value =
                    AuthState.Error("OTP not found. Please request a new OTP.")
            }
        }
    }


    /**
     * Called when user logs out
     */
    fun logout(email: String) {
        logger.logLogout(email)
        otpManager.clearOtp(email)
        _authState.value = AuthState.EnterEmail
    }

    /**
     * Internal helper
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS
            .matcher(email)
            .matches()
    }
}
