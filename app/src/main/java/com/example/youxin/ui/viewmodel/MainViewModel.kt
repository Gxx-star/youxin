package com.example.youxin.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _uiState = MutableStateFlow<Boolean>(false)
    val uiState: MutableStateFlow<Boolean>
        get() = _uiState
    init {
        _uiState.value = savedStateHandle.get<Boolean>("ui_state") ?: false
    }
    fun transData() {
        _uiState.value = !_uiState.value
    }
}