package ca.zeezaglobal.indigorx.Domain.repository


import ca.zeezaglobal.indigorx.Data.mapper.Resource
import ca.zeezaglobal.indigorx.Domain.modal.LoginResult
import ca.zeezaglobal.indigorx.Domain.modal.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(username: String, password: String): Flow<Resource<LoginResult>>
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun clearUser()
    suspend fun isLoggedIn(): Boolean
}