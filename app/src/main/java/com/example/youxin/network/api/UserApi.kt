package com.example.youxin.network.api

import android.util.Log
import com.example.youxin.network.model.ApiResponse
import com.example.youxin.network.model.request.LoginReq
import com.example.youxin.network.model.request.RegisterReq
import com.example.youxin.network.model.request.UpdateUserInfoReq
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
        Log.d("myTag", response.toString())
        if (response.msg != "success") {
            throw Exception("注册失败: ${response.msg}")
        }
        return response.data
    }

    // 登录
    suspend fun login(phone: String, password: String): ApiResponse<LoginResp> {
        val request = LoginReq(phone, password)
        val response = userService.login(request)
        return response
    }

    // 获取用户信息
    suspend fun getUserInfo(token: String): UserinfoResp? {
        val response = userService.getUserInfo(token)
        return response.data
    }

    // 更新个人信息
    suspend fun updateUserInfo(token: String, nickName: String, sex: Byte, avatar: String):Boolean {
        val response =
            userService.updateUserInfo(token, UpdateUserInfoReq(avatar, nickName, sex.toInt()))
        if (response.msg != "success") {
            throw Exception("更新用户信息失败: ${response.msg}")
            return false
        }
        return true
    }
}