package com.example.youxin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatlogs")
data class ChatLogEntity(
    @PrimaryKey
    val id:String,
    val conversationId:String,
    val sendId:String,
    val recvId:String,
    val msgType:Int, // 消息类型
    val msgContent:String, // 消息内容
    val chatType:Int, // 聊天类型
    val sendTime:Long // 发送时间
)
