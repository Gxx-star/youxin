package com.example.youxin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youxin.data.repository.UserRepository
import com.example.youxin.network.api.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val userApi: UserApi,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MeState(null))
    private val _currentUserFlow = userRepository.observeCurrentUser().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    val uiState: StateFlow<MeState> = _uiState
    val currentUserFlow = _currentUserFlow
    fun inputNickname(nickname: String) {
        _uiState.value = _uiState.value.copy(inputNickname = nickname)
    }

    fun selectSex(sex: Byte) {
        _uiState.value = _uiState.value.copy(selectedSex = sex)
    }

    fun uploadAvatar(avatarUri: String) {
        _uiState.value = _uiState.value.copy(selectedAvatar = avatarUri)
    }

    // 初始化MeState
    fun initializeMeState() {
        viewModelScope.launch {
            // 收集最新的用户数据
            _currentUserFlow.collectLatest { currentUser ->
                currentUser?.let { user ->
                    _uiState.update { state ->
                        state.copy(
                            inputNickname = user.nickName.toString(),
                            selectedSex = user.sex,
                            selectedAvatar = user.avatar,
                            // 重置操作状态
                            isLoading = false,
                            errorMessage = null,
                            isSuccess = false
                        )
                    }
                }
            }
        }
    }

    suspend fun updateUserInfo(): Boolean {
        userRepository.updateUserInfo(
            _uiState.value.inputNickname ?: return false,
            _uiState.value.selectedSex ?: return false,
            _uiState.value.selectedAvatar ?: return false,
        )
        return true
    }
}

data class MeState(
    val inputNickname: String? = null,
    val selectedSex: Byte? = null,
    val selectedAvatar: String? = null,
    val isLoading: Boolean? = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean? = false
)
