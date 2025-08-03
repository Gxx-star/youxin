package com.example.youxin.network.model

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T? = null
)