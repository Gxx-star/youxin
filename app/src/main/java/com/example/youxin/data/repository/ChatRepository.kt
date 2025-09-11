package com.example.youxin.data.repository

import android.util.Log
import com.example.youxin.data.db.dao.ChatDao
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.di.DataStoreManager
import com.example.youxin.network.ChatWebSocket
import com.example.youxin.network.api.ChatApi
import com.example.youxin.network.info.MessageContent
import com.example.youxin.network.info.MessageData
import com.example.youxin.network.info.MessageFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.youxin.data.db.entity.ConversationEntity
import java.util.UUID

class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val chatApi: ChatApi,
    private val dataStoreManager: DataStoreManager,
    private val chatWebSocket: ChatWebSocket
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            // 有新消息来时执行
            chatWebSocket.receivedMessages.collect { message ->
                val messageData = message.data ?: run {
                    Log.e("myTag", "收到的消息为空，忽略该消息")
                    return@collect
                }
                val conversationId = UUID.randomUUID().toString()
                val chatLogId = UUID.randomUUID().toString()
                // 如果没有该会话，则为接收方创建会话
                if (chatDao.getConversationByUsersId(message.toId, message.fromId) == null) {
                    chatDao.saveConversation(
                        ConversationEntity(
                            id = conversationId,
                            userId = message.toId,
                            targetId = message.fromId,
                            targetAvatar = "",
                            targetNickname = "",
                            isShow = true,
                            latestChatLog = null,
                            latestChatTime = System.currentTimeMillis(),
                            toRead = 0
                        )
                    )
                }
                chatDao.saveChatLog(
                    ChatLogEntity(
                        id = chatLogId,
                        userId = getUserId(),
                        conversationId = conversationId,
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

    // 发送消息
    suspend fun sendMsg(conversationId: String, targetId: String, msgContent: String) {
        // 服务器发送消息
        chatWebSocket.sendMessage(
            MessageFrame(
                fromId = getUserId(),
                toId = targetId,
                data = MessageData(
                    conversationId,
                    getUserId(),
                    targetId,
                    0,
                    MessageContent(0, msgContent),
                    System.currentTimeMillis()
                )
            )
        )
        val chatLogId = UUID.randomUUID().toString()
        // 本地保存消息
        chatDao.saveChatLog(
            ChatLogEntity(
                id = chatLogId,
                userId = getUserId(),
                conversationId = conversationId,
                sendId = getUserId(),
                recvId = targetId,
                msgType = 0,
                msgContent = msgContent,
                chatType = 0,
                sendTime = System.currentTimeMillis()
            )
        )
        // 更新会话
        chatDao.updateConversation(
            userId = getUserId(),
            conversationId = conversationId,
            chatLog = ChatLogEntity(
                id = chatLogId,
                userId = getUserId(),
                conversationId = conversationId,
                sendId = getUserId(),
                recvId = targetId,
                msgType = 0,
                msgContent = msgContent,
                chatType = 0,
                sendTime = System.currentTimeMillis()
            ),
            currentTime = System.currentTimeMillis()
        )
    }

    suspend fun startConversation(
        myId: String,
        targetId: String,
        targetAvatar: String,
        targetNickname: String,
    ) {
        val conversationId = UUID.randomUUID().toString()
        chatDao.saveConversation(
            ConversationEntity(
                conversationId,
                myId,
                targetId,
                targetAvatar,
                targetNickname,
                true,
                null,
                System.currentTimeMillis(),
                0
            )
        )
    }

    // 查询会话
    fun getConversationIdByUsersId(userId: String, targetId: String): String? {
        val conversation = chatDao.getConversationByUsersId(userId, targetId)
        return conversation?.id
    }

    // 获取会话列表
    suspend fun getConversationsFlow(): Flow<List<ConversationEntity>> {
//        return try {
//            val result = chatApi.getConversations(userId)
//            Result.Success(result)
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
        return chatDao.getConversationsFlowById(getUserId())
    }

    // 通过会话id获取聊天记录
    fun getChatLogsFlow(conversationId: String): Flow<List<ChatLogEntity>> {
        return chatDao.getChatLogsFlowByConversationId(conversationId)
    }
}