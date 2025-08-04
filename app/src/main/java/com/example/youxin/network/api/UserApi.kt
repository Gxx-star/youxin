package com.example.youxin.network.api

import com.example.youxin.network.model.request.LoginReq
import com.example.youxin.network.model.request.RegisterReq
import com.example.youxin.network.model.response.LoginResp
import com.example.youxin.network.model.response.RegisterResp
import com.example.youxin.network.model.response.UserinfoResp
import com.example.youxin.network.service.UserService
import javax.inject.Inject

class UserApi @Inject constructor(
    private val userService: UserService
) {
    //  注册
    suspend fun register(
        phone: String,
        password: String,
        nickname: String,
        sex: Byte = 0,
        avatar: String
    ): RegisterResp? {
        val request = RegisterReq(phone, password, nickname, sex, avatar)
        val response = userService.register(request)
        if (response.message != "success") {
            throw Exception("注册失败: ${response.message}")
        }
        return response.data
    }

    // 登录
    suspend fun login(phone: String, password: String): LoginResp? {
        val request = LoginReq(phone, password)
        val response = userService.login(request)
        if (response.message != "success") {
            throw Exception("登录失败: ${response.message}")
        }
        return response.data
    }
    // 获取用户信息
    suspend fun getUserInfo(token:String): UserinfoResp? {
        val response = userService.getUserInfo(token)
        if (response.message != "success") {
            throw Exception("获取用户信息失败: ${response.message}")
        }
        return response.data
    }
}