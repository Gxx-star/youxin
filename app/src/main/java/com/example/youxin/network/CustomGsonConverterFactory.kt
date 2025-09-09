package com.example.youxin.network

import com.example.youxin.network.model.ApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class CustomGsonConverterFactory(private val gson: Gson) : Converter.Factory() {

    // 处理响应体解析（关键：正确处理ApiResponse<T>的泛型类型）
    override fun responseBodyConverter(
        type: Type, // 目标类型（如ApiResponse<ApplyFriendResp>中的泛型参数类型）
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        // 构建目标类型：ApiResponse<T>（如ApiResponse<ApplyFriendResp>）
        val apiResponseType = TypeToken.getParameterized(ApiResponse::class.java, type).type

        // 创建Gson转换器，明确目标类型为ApiResponse<T>
        val gsonConverter = gson.getAdapter(TypeToken.get(apiResponseType))

        // 返回自定义Converter，强制将响应体解析为ApiResponse<T>
        return Converter<ResponseBody, ApiResponse<*>> { responseBody ->
            try {
                // 解析响应体为ApiResponse<T>
                gsonConverter.fromJson(responseBody.string())
            } finally {
                responseBody.close() // 确保流关闭
            } as ApiResponse<*>?
        }
    }

    companion object {
        // 提供创建方法，方便配置Retrofit
        fun create(gson: Gson = Gson()): CustomGsonConverterFactory {
            return CustomGsonConverterFactory(gson)
        }
    }
}
