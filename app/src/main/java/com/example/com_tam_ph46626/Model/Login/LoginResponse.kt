package com.example.com_tam_ph46626.Model.Login

data class LoginResponse(
    val token: String,
    val userId: String, // Thêm userId vào đây
    val msg: String
)
