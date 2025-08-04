package com.example.youxin.network.service

import com.example.youxin.network.model.ApiResponse
import com.example.youxin.network.model.request.LoginReq
import com.example.youxin.network.model.request.RegisterReq
import com.example.youxin.network.model.response.LoginResp
import com.example.youxin.network.model.response.RegisterResp
import com.example.youxin.network.model.response.UserinfoResp
import com.example.youxin.utils.constant.NetworkConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 用户相关api接口
 */
interface UserService {
    @POST(NetworkConstants.User.REGISTER)
    suspend fun register(@Body request: RegisterReq): ApiResponse<RegisterResp>

    @POST(NetworkConstants.User.LOGIN)
    suspend fun login(@Body request: LoginReq): ApiResponse<LoginResp>

    @GET(NetworkConstants.User.GET_USER_INFO)
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): ApiResponse<UserinfoResp>
}