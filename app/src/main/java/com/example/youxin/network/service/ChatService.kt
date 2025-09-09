package com.example.youxin.network.service

import com.example.youxin.network.model.ApiResponse
import com.example.youxin.network.model.response.GetChatLogsResp
import com.example.youxin.network.model.response.GetConversationsResp
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatService {
    @GET("/v1/im/getConversations")
    suspend fun getConversations(@Query("userId") userId: String): ApiResponse<GetConversationsResp>


    @GET("/v1/im/getChatLog")
    suspend fun getChatLog(
        @Query("startSendTime") startSendTime: Long,
        @Query("endSendTime") endSendTime: Long,
        @Query("count") count: Int,
        @Query("conversationId") conversationId: String,
        @Query("msgId") msgId: String
    ): ApiResponse<GetChatLogsResp>
}