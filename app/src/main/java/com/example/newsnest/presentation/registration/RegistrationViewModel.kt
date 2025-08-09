package com.example.newsnest.presentation.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(RegistrationUiState())
        private set

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.UsernameChanged -> {
                uiState = uiState.copy(username = event.username)
                uiState = if (event.username.length < 3) {
                    uiState.copy(usernameError = "Username must be at least 3 characters long")
                } else {
                    uiState.copy(usernameError = null)
                }
            }

            is RegistrationEvent.EmailChanged -> {
                uiState = uiState.copy(email = event.email)
                uiState = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(event.email).matches()) {
                    uiState.copy(emailError = "Invalid email address")
                } else {
                    uiState.copy(emailError = null)
                }
            }

            is RegistrationEvent.PasswordChanged -> {
                uiState = uiState.copy(password = event.password)
                uiState = if (event.password.length < 8) {
                    uiState.copy(passwordError = "Password must be at least 8 characters long")
                } else {
                    uiState.copy(passwordError = null)
                }
            }

            is RegistrationEvent.ConfirmPasswordChanged -> {
                uiState = uiState.copy(confirmPassword = event.confirmPassword)
                uiState = if (event.confirmPassword != uiState.password) {
                    uiState.copy(confirmPasswordError = "Passwords do not match")
                } else {
                    uiState.copy(confirmPasswordError = null)
                }
            }

            is RegistrationEvent.ImageSelected -> {
                uiState = uiState.copy(selectedImageUri = event.imageUri)
            }

            is RegistrationEvent.Register -> {
                if (uiState.usernameError != null || uiState.emailError != null || uiState.passwordError != null || uiState.confirmPasswordError != null) {
                    uiState =
                        uiState.copy(snackbarMessage = "Please fill the form with valid details")

                    return
                }

            }
        }
    }

    fun clearSnackbarMessage() {
        uiState = uiState.copy(snackbarMessage = null)
    }

}

sealed class RegistrationEvent {
    data class UsernameChanged(val username: String) : RegistrationEvent()
    data class EmailChanged(val email: String) : RegistrationEvent()
    data class PasswordChanged(val password: String) : RegistrationEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistrationEvent()
    data class ImageSelected(val imageUri: String) : RegistrationEvent()
    object Register : RegistrationEvent()
}