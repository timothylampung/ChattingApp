package com.anything.chattingapp.view.auth

data class AuthResult(
    var result: Result? = Result.DEFAULT,
    var message: String? = null
)

enum class Result {
    DEFAULT,
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    REGISTRATION_SUCCESS,
    REGISTRATION_FAILED
}