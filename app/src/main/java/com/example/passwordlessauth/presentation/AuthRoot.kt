package com.example.passwordlessauth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.passwordlessauth.presentation.ErrorScreen
import com.example.passwordlessauth.viewmodel.AuthState
import com.example.passwordlessauth.viewmodel.AuthViewModel

@Composable
fun AuthRoot(viewModel: AuthViewModel) {

    val state by viewModel.authState.collectAsState()

    when (state) {
        is AuthState.EnterEmail -> {
            LoginScreen(
                onSendOtp = viewModel::sendOtp
            )
        }

        is AuthState.EnterOtp -> {
            val otpState = state as AuthState.EnterOtp

            OtpScreen(
                email = otpState.email,
                expiresAt = otpState.expiresAt,
                remainingAttempts = otpState.remainingAttempts,
                errorMessage = otpState.errorMessage,
                onVerifyOtp = { otp ->
                    viewModel.verifyOtp(
                        email = otpState.email,
                        otp = otp
                    )
                },
                onResendOtp = {
                    viewModel.sendOtp(otpState.email)
                }
            )
        }

        is AuthState.LoggedIn -> {
            val loggedIn = state as AuthState.LoggedIn
            SessionScreen(
                email = loggedIn.email,
                sessionStartTime = loggedIn.sessionStartTime,
                onLogout = viewModel::logout
            )
        }

        is AuthState.Error -> {
            ErrorScreen(
                message = (state as AuthState.Error).message,
                onRetry = {
                    // simplest behavior: go back to login
                    viewModel.logout("")
                }
            )
        }
    }
}
