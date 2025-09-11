package com.example.youxin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.data.db.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert
    suspend fun saveChatLog(chatLog: ChatLogEntity): Long

    @Query("select * from chatlogs where conversationId = :id ORDER BY sendTime ASC")
    fun getChatLogsFlowByConversationId(id: String): Flow<List<ChatLogEntity>>

    @Query("SELECT * FROM chatlogs WHERE id = :id")
    fun getChatLogFlowById(id: String): Flow<ChatLogEntity>

    @Insert
    suspend fun saveConversation(conversation: ConversationEntity): Long

    @Query(
        "update conversations set latestChatLog = :chatLog , latestChatTime = :currentTime " +
                "where userId = :userId and id = :conversationId"
    )
    suspend fun updateConversation(
        userId: String,
        conversationId: String,
        chatLog: ChatLogEntity,
        currentTime: Long
    )

    @Query("select * from conversations where userId = :id ORDER BY latestChatTime DESC")
    fun getConversationsFlowById(id: String): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    fun getConversationFlowByConversationId(conversationId: String): Flow<ConversationEntity>

    @Query("SELECT * FROM conversations where userId = :userId and targetId = :targetId")
    fun getConversationByUsersId(userId: String, targetId: String): ConversationEntity?
}