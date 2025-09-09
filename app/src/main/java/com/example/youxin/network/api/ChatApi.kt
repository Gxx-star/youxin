package com.example.youxin.network.api

import com.example.youxin.network.model.ApiResponse
import com.example.youxin.network.model.response.GetChatLogsResp
import com.example.youxin.network.model.response.GetConversationsResp
import com.example.youxin.network.service.ChatService
import javax.inject.Inject

class ChatApi @Inject constructor(
    private val chatService: ChatService
){
    suspend fun getConversations(userId: String): ApiResponse<GetConversationsResp>{
        return chatService.getConversations(userId)
    }
    suspend fun getChatLog(
        startSendTime: Long,
        endSendTime: Long,
        count: Int,
        conversationId: String,
        msgId: String
    ): ApiResponse<GetChatLogsResp>{
        return chatService.getChatLog(startSendTime, endSendTime, count, conversationId, msgId)
    }
}