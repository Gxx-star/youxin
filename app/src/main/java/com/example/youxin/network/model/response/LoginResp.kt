package com.example.youxin.network.model.response

data class LoginResp(
    val expire: Int,
    val id: String,
    val token: String
)