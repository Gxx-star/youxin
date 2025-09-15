package com.example.youxin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 联系人实体类
 */
@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val id: String,
    val nickName: String,
    val avatar: String? = null,
    val sex: Byte,
    val status: FriendStatusEntity
)

/**
 * 好友对于用户的状态实体类
 */
data class FriendStatusEntity(
    val isMuted: Boolean, // 免打扰
    val isTopped: Boolean, // 置顶
    val isBlocked: Boolean,// 拉黑
    val remark: String?// 备注
)

