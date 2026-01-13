package ca.zeezaglobal.indigorx.Domain.usecase.auth

import javax.inject.Inject

data class AuthUseCases @Inject constructor(
    val login: LoginUseCase,
    val logout: LogoutUseCase,
    val getCurrentUser: GetCurrentUserUseCase,
    val checkLoginStatus: CheckLoginStatusUseCase
)