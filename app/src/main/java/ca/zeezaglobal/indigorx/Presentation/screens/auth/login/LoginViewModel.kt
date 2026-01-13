package ca.zeezaglobal.indigorx.Presentation.screens.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.zeezaglobal.indigorx.Data.mapper.Resource
import ca.zeezaglobal.indigorx.Domain.modal.LoginResult

import ca.zeezaglobal.indigorx.Domain.usecase.auth.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent: SharedFlow<LoginNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val isLoggedIn = authUseCases.checkLoginStatus()
            if (isLoggedIn) {
                val user = authUseCases.getCurrentUser()
                _uiState.update { it.copy(isLoggedIn = true, user = user) }
                _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
            }
        }
    }

    fun login(username: String, password: String) {
        // Reset errors
        _uiState.update { it.copy(usernameError = null, passwordError = null, error = null) }

        // Validate inputs
        val usernameError = validateUsername(username)
        val passwordError = validatePassword(password)

        if (usernameError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    usernameError = usernameError,
                    passwordError = passwordError
                )
            }
            return
        }

        // Perform login
        viewModelScope.launch {
            val loginFlow: Flow<Resource<LoginResult>> = authUseCases.login.invoke(username, password)

            loginFlow.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = result.data?.user,
                                isLoggedIn = true,
                                error = null
                            )
                        }
                        _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "An unexpected error occurred"
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearFieldErrors() {
        _uiState.update { it.copy(usernameError = null, passwordError = null) }
    }

    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(username).matches() -> "Invalid email format"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }
}

sealed class LoginNavigationEvent {
    data object NavigateToHome : LoginNavigationEvent()
    data object NavigateToForgotPassword : LoginNavigationEvent()
    data object NavigateToRegister : LoginNavigationEvent()
}