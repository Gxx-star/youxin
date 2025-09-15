package com.example.youxin.network.model.response

import com.example.youxin.network.info.ChatLogInfo
import com.example.youxin.network.info.ConversationInfo

data class GetChatLogsResp(
    val list: List<ChatLogInfo>
)

data class GetConversationsResp(
    val list: Map<String, ConversationInfo>
)