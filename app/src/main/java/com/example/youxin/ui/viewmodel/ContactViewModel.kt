package com.example.youxin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youxin.data.db.entity.ApplyEntity
import com.example.youxin.data.db.entity.ContactEntity
import com.example.youxin.data.db.entity.FriendStatusEntity
import com.example.youxin.data.repository.ContactRepository
import com.example.youxin.di.DataStoreManager
import com.example.youxin.utils.ContactGroupUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    private val _inputRemark = MutableStateFlow("")
    val inputRemark = _inputRemark
    private val _currentSelectedContactId = MutableStateFlow<String?>(null)
    val currentSelectedContactId = _currentSelectedContactId.asStateFlow()
    @OptIn(ExperimentalCoroutinesApi::class)
    val contactStatusFlow: StateFlow<FriendStatusEntity> = currentSelectedContactId
        .flatMapLatest { id -> // 每次id变化都会执行
            if (id == null) {
                flowOf(FriendStatusEntity(false, false, false, null))
            } else {
                contactRepository.getContactStatusFlow(id)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FriendStatusEntity(false, false, false, null)
        )
    fun updateSelectedContact(id: String?) {
        _currentSelectedContactId.value = id
    }

    fun updateRemark(remark: String) {
        _inputRemark.value = remark
    }
    suspend fun deleteFriend(targetId: String) {
        contactRepository.deleteFriend(getUserId()!!, targetId)
    }
    suspend fun syncWithServer() {
        Log.d("myTag", "同步数据")
        contactRepository.syncWithServer()
    }

    suspend fun getUserId(): String? {
        return dataStoreManager.getUserId()
    }

    init {
        viewModelScope.launch {
            contactRepository.currentFriendListFlow.collect { friendList ->
                _currentFriendList.value = ContactGroupUtil.groupContacts(friendList)
            }
        }
        viewModelScope.launch {
            contactRepository.currentApplyListFlow.collect { applyList ->
                _currentApplyList.value = applyList // 申请状态变更时，这里会收到新数据
            }
        }
    }

    suspend fun handleApply(
        applicantId: String,
        applicantNickname: String,
        applicantAvatar: String,
        applicantSex: Byte,
        applicantRemark: String, // 备注
        isApproved: Boolean,
        greetMsg: String
    ) {
        val userId = getUserId()!!
        contactRepository.handleApply(
            applicantId,
            applicantNickname,
            applicantAvatar,
            applicantSex,
            applicantRemark,
            userId,
            isApproved,
            greetMsg
        )
    }

    suspend fun updateContactStatus(
        id: String,
        isMuted: Boolean,
        isTopped: Boolean,
        isBlocked: Boolean,
        remark: String?
    ) {
        contactRepository.updateContactStatus(id, isMuted, isTopped, isBlocked, remark)
    }
}