package com.example.passwordlessauth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SessionScreen(
    email: String,
    sessionStartTime: Long,
    onLogout: (String) -> Unit
) {
    var duration by remember { mutableStateOf(0L) }

    LaunchedEffect(sessionStartTime) {
        while (true) {
            duration = System.currentTimeMillis() - sessionStartTime
            delay(1_000)
        }
    }

    val minutes = (duration / 1000) / 60
    val seconds = (duration / 1000) % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome $email")

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Session duration: %02d:%02d".format(minutes, seconds),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onLogout(email) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
