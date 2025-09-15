package com.example.youxin.data

import com.example.youxin.network.model.ApiResponse

/**
 * 密封类，封装Repository调用api的结果
 */
sealed class Result <out T>{
    // 请求成功时调用构造方法就能把后端的响应转化成Success
    data class Success<out T>(val apiResponse: ApiResponse<@UnsafeVariance T>): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()
}