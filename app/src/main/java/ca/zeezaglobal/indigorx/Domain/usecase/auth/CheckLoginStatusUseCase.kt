package ca.zeezaglobal.indigorx.Domain.usecase.auth


import ca.zeezaglobal.indigorx.Domain.repository.AuthRepository
import javax.inject.Inject

class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}