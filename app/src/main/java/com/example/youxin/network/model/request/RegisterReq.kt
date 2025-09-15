package com.example.youxin.network.model.request

import com.google.gson.annotations.SerializedName

data class RegisterReq(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nickname")
    val nickName: String,
    @SerializedName("sex")
    val sex: Byte,
    @SerializedName("avatar")
    val avatar: String
)