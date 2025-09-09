package com.example.youxin.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.data.repository.ContactRepository
import com.example.youxin.data.repository.UserRepository
import com.example.youxin.di.DataStoreManager
import com.example.youxin.network.ChatWebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 全局ViewModel
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
    val dataStoreManager: DataStoreManager,
    val chatWebSocket: ChatWebSocket
) : ViewModel() {
    private val _currentUser = MutableStateFlow<CurrentUserEntity?>(null)
    val currentUser: StateFlow<CurrentUserEntity?> = _currentUser.asStateFlow()
    val userIdFlow : Flow<String?> = dataStoreManager.getUserIdFlow
    init {
        viewModelScope.launch {
            userRepository.observeCurrentUser().collect {
                _currentUser.value = it  // 观察并同步数据库变化
            }
        }
    }
    suspend fun logout(){
        userRepository.logout()
        contactRepository.logout()
    }
    suspend fun switchUser() {
        userRepository.switchUser()
    }
}