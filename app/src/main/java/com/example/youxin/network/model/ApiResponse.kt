package com.example.youxin.network.model

data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T? = null
)