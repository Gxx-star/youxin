package com.example.youxin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val userId:String,// 所属人的id
    val targetId:String,
    val targetAvatar:String,
    val targetNickname:String,
    val isShow:Boolean,
    val latestChatLog:ChatLogEntity?, // 最后聊天内容
    val latestChatTime:Long,
    val toRead:Int, // 未读消息数
)