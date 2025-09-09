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

    @Query("select * from chatlogs where sendId = :id or recvId = :id")
    suspend fun getChatLogsFlowById(id: String): Flow<List<ChatLogEntity>>

    @Query("SELECT * FROM chatlogs WHERE id = :id")
    suspend fun getChatLogFlowById(id: String): Flow<ChatLogEntity>

    @Insert
    suspend fun saveConversation(conversation: ConversationEntity): Long

    @Query("select * from conversations where userId1 = :id or userId2 = :id")
    suspend fun getConversationsFlowById(id: String): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    suspend fun getConversationFlowByIt(conversationId: String): Flow<ConversationEntity>
}