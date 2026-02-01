package com.example.passwordlessauth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    email: String,
    remainingAttempts: Int,
    expiresAt: Long,
    errorMessage: String?,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit
) {
    var otp by remember { mutableStateOf("") }

    val remainingTime by produceState(
        initialValue = ((expiresAt - System.currentTimeMillis()) / 1000).coerceAtLeast(0),
        expiresAt
    ) {
        while (value > 0) {
            delay(1_000)
            value = ((expiresAt - System.currentTimeMillis()) / 1000).coerceAtLeast(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Enter OTP sent to $email")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Attempts left: $remainingAttempts")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Time left: ${remainingTime}s")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("OTP") },
            singleLine = true
        )

        // 🔴 Inline error message (THIS fixes your issue)
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onVerifyOtp(otp) },
            enabled = otp.length == 6
        ) {
            Text("Verify OTP")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onResendOtp) {
            Text("Resend OTP")
        }
    }
}


