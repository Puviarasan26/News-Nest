package com.example.newsnest.presentation.registration

data class RegistrationUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val selectedImageUri: String? = null,
    val snackbarMessage: String? = null,
    val isLoading: Boolean = false
)
