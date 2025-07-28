package com.example.youxin.network.service

import com.example.youxin.network.model.RegisterReq
import com.example.youxin.network.model.RegisterResp
import com.example.youxin.utils.constant.NetworkConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 用户相关api接口
 */
interface UserService {
    @POST(NetworkConstants.User.REGISTER)
    suspend fun register(@Body request: RegisterReq): Response<RegisterResp>
}