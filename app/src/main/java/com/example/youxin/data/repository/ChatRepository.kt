package com.example.youxin.data.repository

import com.example.youxin.data.db.dao.ChatDao
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.di.DataStoreManager
import com.example.youxin.network.api.ChatApi
import com.example.youxin.network.model.ApiResponse
import com.example.youxin.network.model.response.GetChatLogsResp
import com.example.youxin.network.model.response.GetConversationsResp
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val chatApi: ChatApi,
    private val dataStoreManager: DataStoreManager
) {
    private var cachedUserId: String? = null
    private suspend fun getUserId(): String {
        if (cachedUserId == null) {
            cachedUserId = dataStoreManager.getUserId()
                ?: throw IllegalStateException("用户未登录")
        }
        return cachedUserId!!
    }

    suspend fun getConversations(userId: String): ApiResponse<GetConversationsResp> {
        return chatApi.getConversations(userId)
    }

    suspend fun getChatLogsFlow(
        startSendTime: Long,
        endSendTime: Long,
        count: Int,
        conversationId: String,
        msgId: String
    ): Flow<List<ChatLogEntity>> {
        return chatDao.getChatLogsFlowById(getUserId())
    }
}