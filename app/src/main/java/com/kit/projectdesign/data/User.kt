package com.kit.projectdesign.data

data class User(
    val id: String,
    val username: String,
    val nickname: String,
    val allergies: String // 逗号分隔的字符串
)

data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val password: String, val nickname: String, val allergies: String)
