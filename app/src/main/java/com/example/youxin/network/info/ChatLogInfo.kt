package com.example.youxin.network.info

data class ChatLogInfo(
    val chatType: String,
    val conversationId: String,
    val id: String,
    val msgType: String,
    val recvId: String,
    val sendId: String,
    val sendTime: String
)