package com.example.youxin.network.model

import com.google.gson.annotations.SerializedName

class RegisterResp (
    @SerializedName("token")
    val token: String,
    @SerializedName("expire")
    val expire: Long,
)