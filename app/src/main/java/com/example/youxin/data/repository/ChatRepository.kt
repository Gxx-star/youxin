package com.example.youxin.data.repository

import com.alibaba.sdk.android.oss.common.OSSLogToFileUtils.init
import com.example.youxin.data.db.dao.ChatDao
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.di.DataStoreManager
import com.example.youxin.network.ChatWebSocket
import com.example.youxin.network.api.ChatApi
import com.example.youxin.network.info.MessageContent
import com.example.youxin.network.info.MessageData
import com.example.youxin.network.info.MessageFrame
import com.example.youxin.network.model.ApiResponse
import com.example.youxin.network.model.response.GetChatLogsResp
import com.example.youxin.network.model.response.GetConversationsResp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val chatApi: ChatApi,
    private val dataStoreManager: DataStoreManager,
    private val chatWebSocket: ChatWebSocket
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            chatWebSocket.receivedMessages.collect { message ->
                chatDao.saveChatLog(
                    ChatLogEntity(
                        id = message.id,
                        conversationId = message.data.conversationId,
                        sendId = message.fromId,
                        recvId = message.toId,
                        msgType = message.data.msg.msgType,
                        msgContent = message.data.msg.msgContent,
                        chatType = message.data.chatType,
                        sendTime = message.data.sendTime
                    )
                )
            }
        }
    }

    private var cachedUserId: String? = null
    private suspend fun getUserId(): String {
        if (cachedUserId == null) {
            cachedUserId = dataStoreManager.getUserId()
                ?: throw IllegalStateException("用户未登录")
        }
        return cachedUserId!!
    }
    private suspend fun getConnectionState(): Boolean {
        return chatWebSocket.connectionState.first()
    }
    suspend fun sendMsg(message: ChatLogEntity){
        chatWebSocket.sendMessage(MessageFrame(
            fromId = message.sendId,
            toId = message.recvId,
            data = MessageData(
                message.conversationId,
                message.sendId,
                message.recvId,
                message.chatType,
                MessageContent(message.msgType,message.msgContent),
                message.sendTime
            )
        ))
        chatDao.saveChatLog(message)
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