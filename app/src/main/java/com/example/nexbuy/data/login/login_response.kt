package com.example.nexbuy.data.login

data class login_response(
    val message: String?,
    val token: String?,
    val user: User?
)

data class User(
    val address: Any?,
    val email: String?,
    val id: Int?,
    val is_active: Int?,
    val is_verified: Int?,
    val name: String?,
    val nid: Any?,
    val phone: String?
)