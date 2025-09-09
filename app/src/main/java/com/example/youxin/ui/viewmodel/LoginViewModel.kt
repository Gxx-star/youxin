package com.example.youxin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.data.repository.UserRepository
import com.example.youxin.network.ChatWebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 登录页面ViewModel
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    val chatWebSocket: ChatWebSocket
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    private val _currentUser = MutableStateFlow<CurrentUserEntity?>(null)
    val currentUser: StateFlow<CurrentUserEntity?> = _currentUser.asStateFlow()

    // 标记currentUser是否正在加载
    private var _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.observeCurrentUser().collect {
                _currentUser.value = it
                _isLoading.value = false
            }
        }
    }

    // 更新手机号输入
    fun updatePhone(phone: String) {
        _state.value = _state.value.copy(phone = phone)
    }

    // 更新密码输入
    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun login() {
        viewModelScope.launch {
            // 快照
            val currentUser = currentUser.value
            var currentState = _state.value
            // 开始登录
            _state.value = _state.value.copy(isLoading = true)
            if (currentUser != null) {
                currentState = currentState.copy(phone = currentUser.phone)
            }
            if (userRepository.login(currentState.phone, currentState.password) != null) {
                _state.value = currentState.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } else {
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "登录失败"
                )
            }
        }
    }

    suspend fun logout() {
        userRepository.logout()
    }

    suspend fun switchUser() {
        userRepository.switchUser()
    }

    // 重置状态
    fun resetState() {
        _state.value = LoginState() // 重置页面状态（包括 isSuccess = false)
    }
}

data class LoginState(
    val phone: String = "",
    val password: String = "",
    val isLoading: Boolean = false, // 防止快速点击等
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)