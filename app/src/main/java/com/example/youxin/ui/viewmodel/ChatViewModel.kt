package com.example.youxin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.data.db.entity.ConversationEntity
import com.example.youxin.data.repository.ChatRepository
import com.example.youxin.network.ChatWebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatWebSocket: ChatWebSocket
) : ViewModel() {
    val scope = viewModelScope
    private val _conversations = MutableStateFlow<List<ConversationEntity>>(emptyList())
    val conversations: StateFlow<List<ConversationEntity>> = _conversations.asStateFlow()
    private val _currentConversationId = MutableStateFlow<String?>(null)
    val currentConversationId = _currentConversationId.asStateFlow()
    @OptIn(ExperimentalCoroutinesApi::class)
    val chatLogsFlow: StateFlow<List<ChatLogEntity>> = _currentConversationId.flatMapLatest {
        if (it != null) {
            chatRepository.getChatLogsFlow(it)
        } else {
            MutableStateFlow(emptyList())
        }
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        scope.launch {
            chatRepository.getConversationsFlow().collect {
                _conversations.value = it
            }
        }
    }

    fun updateCurrentConversationId(conversationId: String) {
        _currentConversationId.value = conversationId
    }

    fun getConversationIdByUsersId(userId: String, targetId: String): String? {
        return chatRepository.getConversationIdByUsersId(userId, targetId)
    }

    suspend fun startConversation(
        myId: String,
        targetId: String,
        targetAvatar: String,
        targetNickname: String,
    ) {
        chatRepository.startConversation(myId, targetId, targetAvatar, targetNickname)
    }

    suspend fun sendMsg(conversationId: String, targetId: String, msgContent: String) {
        chatRepository.sendMsg(conversationId, targetId, msgContent)
    }
}