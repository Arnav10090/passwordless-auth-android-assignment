package com.example.passwordlessauth.data

import timber.log.Timber

class AnalyticsLogger {

    fun logOtpGenerated(email: String) {
        Timber.i("OTP generated for email=$email")
    }

    fun logOtpSuccess(email: String) {
        Timber.i("OTP validation SUCCESS for email=$email")
    }

    fun logOtpFailure(email: String, reason: String) {
        Timber.w("OTP validation FAILURE for email=$email reason=$reason")
    }

    fun logLogout(email: String) {
        Timber.i("User logged out email=$email")
    }
}
