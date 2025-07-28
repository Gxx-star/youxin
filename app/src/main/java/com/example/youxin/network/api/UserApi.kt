package com.example.youxin.network.api

import com.example.youxin.network.model.RegisterReq
import com.example.youxin.network.model.RegisterResp
import com.example.youxin.network.service.UserService
import javax.inject.Inject

class UserApi @Inject constructor(
    private val userService: UserService,
) {
    suspend fun register(
        phone: String,
        password: String,
        nickname: String,
        sex: Byte = 0,
        avatar: String
    ): RegisterResp {
        val request = RegisterReq(phone, password, nickname, sex, avatar)
        val response = userService.register(request)
        if (!response.isSuccessful) {
            throw Exception("注册失败: ${response.message()}")
        }
        return response.body() ?: throw Exception("注册响应数据为空")
    }
}