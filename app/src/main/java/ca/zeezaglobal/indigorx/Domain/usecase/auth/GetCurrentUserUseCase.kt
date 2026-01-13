package ca.zeezaglobal.indigorx.Domain.usecase.auth


import ca.zeezaglobal.indigorx.Domain.modal.User
import ca.zeezaglobal.indigorx.Domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? {
        return authRepository.getUser()
    }
}