package com.example.youxin.network.info

import com.google.gson.annotations.SerializedName

/**
 * 群聊状态
 */
class GroupStatus (
    @SerializedName("is_muted")
    val isMuted: Boolean, // 群消息免打扰
    @SerializedName("is_topped")
    val isTopped: Boolean, // 群置顶
    @SerializedName("remark")
    val remark: String? // 群备注
)