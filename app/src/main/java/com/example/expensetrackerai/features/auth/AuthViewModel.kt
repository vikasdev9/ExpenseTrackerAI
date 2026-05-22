package com.example.expensetrackerai.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerai.core.security.BiometricAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val biometricManager: BiometricAuthManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState = _authState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AuthUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onSplashFinished() {
        // Logic to determine if user should go to Onboarding or Login
        _authState.value = AuthState.Onboarding
    }

    fun onOnboardingFinished() {
        _authState.value = AuthState.Authenticated
    }

    fun onLoginSuccess() {
        _authState.value = AuthState.Authenticated
    }

    sealed interface AuthState {
        object Initial : AuthState
        object Onboarding : AuthState
        object Authenticated : AuthState
    }

    sealed interface AuthUiEvent {
        object NavigateToDashboard : AuthUiEvent
    }
}
