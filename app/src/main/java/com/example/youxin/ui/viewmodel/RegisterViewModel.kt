package com.example.youxin.ui.viewmodel

import android.R.attr.password
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
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

    @OptIn(UnstableApi::class)
    fun register() {
        val uiState = _state.value // 中间变量，快照保存_state处理多线程的情况
        Log.d("myTag", uiState.toString())
        if (uiState.phone.isEmpty() || uiState.password.isEmpty() || uiState.nickname.isEmpty()) {
            _state.value = uiState.copy(errorMessage = "请填写完整信息")
            Log.d("myTag", "信息不完整")
            return
        }
        // 一些其他的筛选步骤
        if (!uiState.phone.isValidPhone() || uiState.nickname.length > 10 || uiState.password.length > 20) {
            _state.value = uiState.copy(errorMessage = "请填写正确的信息")
            Log.d("myTag", "信息错误")
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
                Log.d("myTag", "注册成功")
                _state.value = uiState.copy(isLoading = false, isSuccess = true)
            } else {

                Log.d("myTag", "注册失败")
                _state.value = uiState.copy(isLoading = false, errorMessage = "注册失败")
            }
        }
    }

    // 保存图片到应用私有目录
    fun saveAvatarToPrivateDir(context: Context, uri: Uri): String? {
        return try {
            // 1. 创建应用私有目录文件（不会被系统回收）
            val fileName = "avatar_${System.currentTimeMillis()}.jpg"
            val privateFile = File(context.filesDir, fileName) // 内部存储目录

            // 2. 复制图片内容到私有文件
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                privateFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream) // 复制文件
                }
            }

            // 3. 保存私有文件的 URI（持久有效）
            Uri.fromFile(privateFile).toString()
        } catch (e: Exception) {
            e.printStackTrace() // 处理异常（如文件读写失败）
            null
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
        val avatar: String = "https://i.postimg.cc/W4qrZcBH/default-avatar.png",
        val isLoading: Boolean = false, // 防止快速点击等
        val errorMessage: String? = null,
        val isSuccess: Boolean = false
    )

    // 手机号验证扩展函数
    private fun String.isValidPhone(): Boolean {
        return length == 11 && matches(Regex("1[3-9]\\d{9}"))
    }
}