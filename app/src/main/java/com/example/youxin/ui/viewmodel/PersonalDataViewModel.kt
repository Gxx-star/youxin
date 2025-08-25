package com.example.youxin.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.youxin.data.repository.UserRepository
import com.example.youxin.di.DataStoreManager
import com.example.youxin.network.api.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userApi: UserApi,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    // 加载状态
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    // 错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    init{

    }
}