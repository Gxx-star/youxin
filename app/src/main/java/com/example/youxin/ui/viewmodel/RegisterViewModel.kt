package com.example.youxin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youxin.data.repository.UserRepository
import com.example.youxin.ui.viewmodel.isValidPhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  注册页面ViewModel
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()
    fun updatePhone(phone: String) {
        _state.value = _state.value.copy(phone = phone)
    }

    fun updateNickname(nickname: String) {
        _state.value = _state.value.copy(nickname = nickname)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun updateSex(sex: Byte) {
        _state.value = _state.value.copy(sex = sex)
    }

    fun updateAvatar(avatar: String) {
        _state.value = _state.value.copy(avatar = avatar)
    }

    fun register() {
        val uiState = _state.value // 中间变量，快照保存_state处理多线程的情况
        if (uiState.phone.isEmpty() || uiState.password.isEmpty() || uiState.nickname.isEmpty()) {
            _state.value = uiState.copy(errorMessage = "请填写完整信息")
            return
        }
        // 一些其他的筛选步骤
        if (!uiState.phone.isValidPhone() || uiState.nickname.length > 10 || uiState.password.length > 20) {
            _state.value = uiState.copy(errorMessage = "请填写正确的信息")
            return
        }
        // 执行注册
        viewModelScope.launch {
            _state.value = uiState.copy(isLoading = true, errorMessage = null)
            val result = userRepository.register(
                phone = uiState.phone,
                password = uiState.password,
                nickname = uiState.nickname,
                sex = uiState.sex,
                avatar = uiState.avatar
            )
            if (result != null) {
                _state.value = uiState.copy(isLoading = false, isSuccess = true)
            } else {
                _state.value = uiState.copy(isLoading = false, errorMessage = "注册失败")
            }
        }
    }
}

/**
 * 注册页面的状态
 */
data class RegisterState(
    val phone: String = "",
    val nickname: String = "",
    val password: String = "",
    val sex: Byte = 0,// 0未知1男2女
    val avatar: String = "https://i.postimg.cc/fTwW9p3r/default-avatar.png",
    val isLoading: Boolean = false, // 防止快速点击等
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

// 手机号验证扩展函数
private fun String.isValidPhone(): Boolean {
    return length == 11 && matches(Regex("1[3-9]\\d{9}"))
}