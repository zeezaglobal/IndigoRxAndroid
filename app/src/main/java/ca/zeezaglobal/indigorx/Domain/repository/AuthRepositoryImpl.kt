package ca.zeezaglobal.indigorx.Domain.repository




import ca.zeezaglobal.indigorx.Data.local.TokenManager
import ca.zeezaglobal.indigorx.Data.mapper.Resource
import ca.zeezaglobal.indigorx.Data.mapper.toDomain
import ca.zeezaglobal.indigorx.Data.remote.api.AuthApi
import ca.zeezaglobal.indigorx.Data.remote.dto.LoginRequestDto
import ca.zeezaglobal.indigorx.Domain.modal.LoginResult
import ca.zeezaglobal.indigorx.Domain.modal.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override fun login(username: String, password: String): Flow<Resource<LoginResult>> = flow {
        emit(Resource.Loading())

        try {
            val response = authApi.login(LoginRequestDto(username = username, password = password))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val loginResult = body.toDomain()

                    // Save token and user to local storage
                    tokenManager.saveToken(loginResult.token)
                    tokenManager.saveUser(loginResult.user)

                    emit(Resource.Success(loginResult))
                } else {
                    emit(Resource.Error(body?.message ?: "Login failed"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Invalid credentials"
                    403 -> "Account not authorized"
                    404 -> "User not found"
                    500 -> "Server error. Please try again later"
                    else -> "Login failed: ${response.message()}"
                }
                emit(Resource.Error(errorMessage))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error. Please check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
        }
    }

    override suspend fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }

    override suspend fun getToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun clearToken() {
        tokenManager.clearToken()
    }

    override suspend fun saveUser(user: User) {
        tokenManager.saveUser(user)
    }

    override suspend fun getUser(): User? {
        return tokenManager.getUser()
    }

    override suspend fun clearUser() {
        tokenManager.clearUser()
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
}