package com.example.newsnest.presentation.registration

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegistrationScreen(onNavigate: () -> Unit, viewModel: RegistrationViewModel = hiltViewModel()) {
    val state = viewModel.uiState
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.onEvent(RegistrationEvent.ImageSelected(uri.toString()))
    }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Registration Screen", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (state.selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(state.selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "Add\nPhoto",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.username,
                onValueChange = {
                    viewModel.onEvent(
                        RegistrationEvent.UsernameChanged(it)
                    )
                },
                label = { Text("Username") },
                isError = state.usernameError != null,
                supportingText = {
                    if (state.usernameError != null) {
                        Text(text = state.usernameError, color = MaterialTheme.colorScheme.error)
                    }
                })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.email, onValueChange = {
                    viewModel.onEvent(
                        RegistrationEvent.EmailChanged(it)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                label = { Text("Email") },
                isError = state.emailError != null,
                supportingText = {
                    if (state.emailError != null) {
                        Text(text = state.emailError, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    viewModel.onEvent(
                        RegistrationEvent.PasswordChanged(it)
                    )
                },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = state.passwordError != null,
                supportingText = {
                    if (state.passwordError != null) {
                        Text(text = state.passwordError, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = {
                    viewModel.onEvent(
                        RegistrationEvent.ConfirmPasswordChanged(it)
                    )
                },
                label = { Text("Confirm Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = state.confirmPasswordError != null,
                supportingText = {
                    if (state.confirmPasswordError != null) {
                        Text(
                            text = state.confirmPasswordError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.onEvent(RegistrationEvent.Register)
            }) {
                Text(text = "Register")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Already have an account? Login",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onNavigate.invoke()
                }
            )
        }
    }

}