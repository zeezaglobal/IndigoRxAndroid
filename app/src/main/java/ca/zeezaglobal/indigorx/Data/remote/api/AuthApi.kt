package ca.zeezaglobal.indigorx.Data.remote.api

import ca.zeezaglobal.indigorx.Data.remote.dto.LoginRequestDto
import ca.zeezaglobal.indigorx.Data.remote.dto.LoginResponseDto
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>
}