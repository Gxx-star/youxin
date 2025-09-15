package com.example.youxin.network.info

import com.google.gson.annotations.SerializedName

/**
 * 用户实体类
 */
class BaseUserInfo (
    @SerializedName("user_id")
    val userId:String, // 用户id
    @SerializedName("nickname")
    val nickName:String,
    @SerializedName("avatar_uri")
    val avatarUri:String,
    @SerializedName("gender")
    val gender:Int
)