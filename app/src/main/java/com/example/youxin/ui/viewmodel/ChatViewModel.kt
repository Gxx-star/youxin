package com.example.youxin.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.data.repository.ChatRepository
import com.example.youxin.data.repository.ContactRepository
import com.example.youxin.network.ChatWebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class ChatViewModel@Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatWebSocket: ChatWebSocket
) :ViewModel(){
    suspend fun getConnectionState(): Boolean {
        return chatWebSocket.connectionState.first()
    }
    suspend fun sendMsg(message: ChatLogEntity) {
        chatRepository.sendMsg(message)
    }
}