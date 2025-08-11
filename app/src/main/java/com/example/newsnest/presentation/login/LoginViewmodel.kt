package com.example.newsnest.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsnest.domain.usecase.LoginUseCase
import com.example.newsnest.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun onLogin() {
        uiState = uiState.copy(isLoading = true)
        loginUseCase(uiState.email, uiState.password).onEach { result ->
            when (result) {
                is Result.Success -> {
                    uiState = uiState.copy(isLoading = false)
                }

                is Result.Error -> {
                    uiState = uiState.copy(isLoading = false, errorMessage = result.message)
                }

                is Result.Loading -> {
                    uiState = uiState.copy(isLoading = true)
                }

            }

        }.launchIn(viewModelScope)
    }

}