package com.example.newsnest.domain.repo

import com.example.newsnest.utils.Result

interface AuthRepository {
    suspend fun registerUser(
        email: String,
        password: String,
        imageUri: String,
        username: String
    ): Result<Unit>

    suspend fun loginUser(email: String, password: String): Result<Unit>

}