package com.example.passwordlessauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordlessauth.ui.AuthRoot
import com.example.passwordlessauth.ui.theme.PasswordlessAuthTheme
import com.example.passwordlessauth.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasswordlessAuthTheme {
                val viewModel: AuthViewModel = viewModel()
                AuthRoot(viewModel)
            }
        }
    }
}
