package com.example.youxin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val userId1:String,
    val userId2:String,
    val isShow:Boolean,
    val latestChatLog:ChatLogEntity, // 最后聊天内容
    val toRead:Int, // 未读消息数
)