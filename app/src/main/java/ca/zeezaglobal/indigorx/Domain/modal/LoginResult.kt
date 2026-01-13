package ca.zeezaglobal.indigorx.Domain.modal

data class LoginResult(
    val user: User,
    val token: String
)