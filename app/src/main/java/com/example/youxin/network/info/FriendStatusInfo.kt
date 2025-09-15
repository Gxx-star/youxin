package com.example.youxin.network.info

import com.google.gson.annotations.SerializedName

/**
 * 好友状态实体类
 */
class FriendStatusInfo (
    @SerializedName("is_muted")
    val isMuted: Boolean, // 免打扰
    @SerializedName("is_topped")
    val isTopped: Boolean, // 置顶
    @SerializedName("is_blocked")
    val isBlocked: Boolean, // 拉黑
    @SerializedName("remark")
    val remark: String?
)