package com.example.newsnest.domain.usecase

import com.example.newsnest.domain.repo.AuthRepository
import com.example.newsnest.utils.Result
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RegisterUsecase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String, imageUri: String, username: String) = flow {
        emit(Result.Loading)
        val result = authRepository.registerUser(email, password, imageUri, username)
        emit(result)
    }
}