package com.example.youxin.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 申请好友的响应
 */
data class ApplyFriendResp(
    @SerializedName("apply_id")
    val applyId: String,
    @SerializedName("apply_time")
    val applyTime: Int
)

/**
 * 获取申请请求列表
 */
data class GetApplyListResp(
    val list: List<Apply>?
)

data class Apply(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("greet_msg")
    val greetMsg: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("user_id")
    val userId: String
)

/**
 * 获取好友列表
 */
data class GetFriendListResp(
    @SerializedName("friend_list")
    val friendList: List<Friend>
)

data class Friend(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val gender: Int,
    val nickname: String,
    val status: Status,
    @SerializedName("user_id")
    val userId: String
)

data class Status(
    @SerializedName("is_blocked")
    val isBlocked: Boolean,
    @SerializedName("is_muted")
    val isMuted: Boolean,
    @SerializedName("is_topped")
    val isTopped: Boolean,
    @SerializedName("remark")
    val remark: String
)

/**
 * 查询用户响应
 */
data class FindUserResp(
    val infos: List<UserInfo>
)

data class UserInfo(
    val avatar: String,
    val id: String,
    val nickname: String,
    val phone: String,
    val sex: Int
)
/**
 * 修改个人信息响应
 */
data class UpdateUserInfoResp(
    val userInfo: UserInfo
)
