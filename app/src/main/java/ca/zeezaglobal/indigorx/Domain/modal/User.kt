package ca.zeezaglobal.indigorx.Domain.modal



data class User(
    val id: Int,
    val name: String,
    val email: String,
    val userType: UserType,
    val status: UserStatus,
    val specialization: String?,
    val licenseNumber: String?,
    val isProfileComplete: Boolean,
    val isEmailVerified: Boolean,
    val isValidated: Boolean
)

enum class UserType {
    DOCTOR,
    PHARMACIST,
    ADMIN,
    UNKNOWN;

    companion object {
        fun fromString(value: String): UserType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}

enum class UserStatus {
    ACTIVE,
    INACTIVE,
    PENDING,
    SUSPENDED,
    UNKNOWN;

    companion object {
        fun fromString(value: String): UserStatus {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}