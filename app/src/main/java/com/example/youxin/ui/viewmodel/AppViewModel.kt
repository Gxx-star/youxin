package com.example.youxin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val userRepository: UserRepository
) : ViewModel() {
    private val _currentUser = MutableStateFlow<CurrentUserEntity?>(null)
    val currentUser: StateFlow<CurrentUserEntity?> = _currentUser.asStateFlow()

    init {
        viewModelScope.launch {
            _currentUser.value = userRepository.getCurrentUser()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _currentUser.value = null
        }
    }
}