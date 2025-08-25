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