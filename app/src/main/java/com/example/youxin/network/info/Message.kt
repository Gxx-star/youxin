package com.example.youxin.network.info

import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * 顶层消息帧数据类
 */
data class MessageFrame(
    val frameType: Int,
    val id: String,
    val toId: String,
    val fromId: String,
    val method: String,
    val data: MessageData
) {
    constructor(toId: String, fromId: String, data: MessageData) : this(
        0,
        UUID.randomUUID().toString(),
        toId,
        fromId,
        "conversation.chat",
        data
    )
}

data class MessageData(
    @SerializedName("ConversationId")
    val conversationId: String = "",
    @SerializedName("SendId")
    val sendId: String,
    @SerializedName("RecvId")
    val recvId: String,
    @SerializedName("ChatType")
    val chatType: Int,
    @SerializedName("Msg")
    val msg: MessageContent,
    @SerializedName("SendTime")
    val sendTime: Long = 0L
)

/**
 * 消息具体内容
 */
data class MessageContent(
    @SerializedName("MsgType")
    val msgType: Int,
    @SerializedName("MsgContent")
    val msgContent: String
)