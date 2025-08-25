package com.example.youxin.network.info

import com.google.gson.annotations.SerializedName

/**
 * 某个好友的实体类
 */
class UserInfo (
    @SerializedName("user_id")
    val userId:String, // 用户id
    @SerializedName("nickname")
    val nickName:String,
    @SerializedName("avatar_uri")
    val avatarUri:String,
    @SerializedName("gender")
    val gender:Int,
    @SerializedName("status")
    val status:FriendStatusInfo //对于该好友的状态
)