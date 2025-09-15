package com.example.youxin.network

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlin.jvm.java

data class ApiError(
    val code: Int,         // 业务错误码（如1001：手机号格式错误）
    val message: String,   // 错误提示信息
    val details: Map<String, String>?= null // 字段级错误详情
)

// 自定义异常：用于将拦截器解析的错误传递给上层
class BadRequestException(
    val apiError: ApiError,  // 携带解析后的错误信息
    message: String = apiError.message
) : Exception(message)

class BadRequestInterceptor : Interceptor {
    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        // 执行原始请求
        val originalRequest = chain.request()
        val originalResponse = chain.proceed(originalRequest)

        // 仅处理400状态码
        if (originalResponse.code == 400) {
            return handle400Error(originalResponse)
        }

        // 非400状态直接返回原始响应
        return originalResponse
    }

    private fun handle400Error(originalResponse: Response): Response {
        try {
            // 读取错误体（注意：string()只能调用一次，需保存为字符串）
            val errorBodyStr = originalResponse.body?.string() ?: ""
            if (errorBodyStr.isEmpty()) {
                // 错误体为空时，构建默认错误
                val defaultError = ApiError(
                    code = 400,
                    message = "请求参数错误，请检查输入"
                )
                // 抛出自定义异常（上层可捕获）
                throw BadRequestException(defaultError)
            }

            // 解析错误体为ApiError对象
            val apiError = gson.fromJson(errorBodyStr, ApiError::class.java)
            Log.d("myTag", "拦截")
            // 此处可添加通用处理逻辑（如日志记录）
            android.util.Log.e("400 Error", "Code: ${apiError.code}, Msg: ${apiError.message}")

            // 重新构建响应体（因为originalResponse的errorBody已被消耗）
            val mediaType =
                originalResponse.body?.contentType() ?: "application/json".toMediaTypeOrNull()
            val newResponseBody = errorBodyStr.toResponseBody(mediaType)

            // 返回新响应，或抛异常由上层处理（二选一，根据需求）
            // 方案1：返回修改后的响应（上层仍需判断code）
            return originalResponse.newBuilder()
                .body(newResponseBody)
                .build()

            // 方案2：直接抛出自定义异常（上层通过try-catch捕获）
            // throw BadRequestException(apiError)

        } catch (e: Exception) {
            // 解析错误体失败（如JSON格式不匹配）
            val parseError = ApiError(
                code = 400,
                message = "请求参数错误，解析失败"
            )
            throw BadRequestException(parseError)
        }
    }
}