package ca.zeezaglobal.indigorx.Data.remote.dto



import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("user")
    val user: UserDto?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("message")
    val message: String?
)

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("userType")
    val userType: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("specialization")
    val specialization: String?,
    @SerializedName("licenseNumber")
    val licenseNumber: String?,
    @SerializedName("profileComplete")
    val profileComplete: Boolean,
    @SerializedName("emailVerified")
    val emailVerified: Boolean,
    @SerializedName("isValidated")
    val isValidated: Int
)