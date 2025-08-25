package com.example.youxin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youxin.data.db.entity.ApplyEntity
import com.example.youxin.data.db.entity.ContactEntity
import com.example.youxin.data.repository.ContactRepository
import com.example.youxin.di.DataStoreManager
import com.example.youxin.utils.ContactGroupUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _currentFriendList = MutableStateFlow<Map<String, List<ContactEntity>>>(emptyMap())
    val currentFriendList = _currentFriendList
    private val _currentApplyList = MutableStateFlow<List<ApplyEntity>>(emptyList())
    val currentApplyList = _currentApplyList
    suspend fun getUserId(): String? {
        return dataStoreManager.getUserId()
    }

    init {
        viewModelScope.launch {
            contactRepository.currentFriendListFlow.collect {
                _currentFriendList.value = ContactGroupUtil.groupContacts(it)
                contactRepository.currentApplyListFlow.collect {
                    _currentApplyList.value = it
                }
            }
        }
    }

    suspend fun handleApply(
        applicantId: String,
        applicantNickname: String,
        applicantAvatar: String,
        applicantSex: Byte,
        applicantRemark: String, // 备注
        isApproved: Boolean
    ) {
        val userId = getUserId()!!
        contactRepository.handleApply(
            applicantId,
            applicantNickname,
            applicantAvatar,
            applicantSex,
            applicantRemark,
            userId,
            isApproved
        )
    }
}