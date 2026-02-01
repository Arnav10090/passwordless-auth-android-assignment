package com.example.passwordlessauth.data

sealed class OtpResult {
    object Success : OtpResult()
    object Expired : OtpResult()
    object Invalid : OtpResult()
    object AttemptsExceeded : OtpResult()
    object NotFound : OtpResult()
}
