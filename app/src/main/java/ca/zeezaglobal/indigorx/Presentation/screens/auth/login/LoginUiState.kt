package ca.zeezaglobal.indigorx.Presentation.screens.auth.login

import ca.zeezaglobal.indigorx.Domain.modal.User


data class LoginUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null
)