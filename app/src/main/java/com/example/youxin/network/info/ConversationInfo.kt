package com.example.youxin.network.info

data class ConversationInfo(
    val chatType: String,
    val conversationId: String,
    val isShow: String,
    val msg: MessageData,
    val read: String,
    val seq: String,
    val targetId: String,
    val toRead: String,
    val total: String
)