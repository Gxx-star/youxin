package com.example.youxin.network.model.response

data class UserinfoResp(
    val info: Info
)

data class Info(
    val avatar: String,
    val id: String,
    val nickname: String,
    val phone: String,
    val sex: Byte
)