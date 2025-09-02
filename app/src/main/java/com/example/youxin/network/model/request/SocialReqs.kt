package com.example.youxin.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 申请好友请求
 */
data class ApplyFriendReq(
    @SerializedName("applicant_id")
    val applicantId: String,
    @SerializedName("greet_msg")
    val greetMsg: String,
    @SerializedName("target_id")
    val targetId: String
)

/**
 * 处理申请好友请求
 */
data class HandleApplyReq(
    @SerializedName("applicant_id")
    val applicantId: String,
    @SerializedName("is_approved")
    val isApproved: Boolean,
    @SerializedName("target_id")
    val targetId: String
)

/**
 * 更新个人信息请求
 */
data class UpdateUserInfoReq(
    val avatar: String,
    val nickname: String,
    val sex: Int
)

/**
 * 更新好友状态请求
 */
data class UpdateFriendStatusReq(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("friend_id")
    val friendId: String,
    val status: Status
)

data class Status(
    @SerializedName("is_blocked")
    val isBlocked: Boolean,
    @SerializedName("is_muted")
    val isMuted: Boolean,
    @SerializedName("is_topped")
    val isTopped: Boolean,
    val remark: String?
)