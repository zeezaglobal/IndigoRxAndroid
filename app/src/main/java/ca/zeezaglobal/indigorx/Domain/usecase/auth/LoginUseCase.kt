package ca.zeezaglobal.indigorx.Domain.usecase.auth


import ca.zeezaglobal.indigorx.Data.mapper.Resource
import ca.zeezaglobal.indigorx.Domain.modal.LoginResult
import ca.zeezaglobal.indigorx.Domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(username: String, password: String): Flow<Resource<LoginResult>> {
        return authRepository.login(username, password)
    }
}