package ca.zeezaglobal.indigorx.Data.mapper

import ca.zeezaglobal.indigorx.Data.remote.dto.LoginResponseDto
import ca.zeezaglobal.indigorx.Data.remote.dto.UserDto
import ca.zeezaglobal.indigorx.Domain.modal.LoginResult
import ca.zeezaglobal.indigorx.Domain.modal.User
import ca.zeezaglobal.indigorx.Domain.modal.UserStatus
import ca.zeezaglobal.indigorx.Domain.modal.UserType

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        userType = UserType.fromString(userType),
        status = UserStatus.fromString(status),
        specialization = specialization,
        licenseNumber = licenseNumber,
        isProfileComplete = profileComplete,
        isEmailVerified = emailVerified,
        isValidated = isValidated == 1
    )
}

fun LoginResponseDto.toDomain(): LoginResult {
    requireNotNull(user) { "User cannot be null for successful login" }
    requireNotNull(token) { "Token cannot be null for successful login" }

    return LoginResult(
        user = user.toDomain(),
        token = token
    )
}