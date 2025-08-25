package com.example.youxin.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.example.youxin.data.db.AppDatabase
import com.example.youxin.data.db.dao.ApplyDao
import com.example.youxin.data.db.dao.ContactDao
import com.example.youxin.data.db.entity.ApplyEntity
import com.example.youxin.data.db.entity.ContactEntity
import com.example.youxin.data.db.entity.FriendStatusEntity
import com.example.youxin.di.DataStoreManager
import com.example.youxin.network.api.SocialApi
import com.example.youxin.network.model.response.Friend
import com.example.youxin.network.model.response.GetApplyListResp
import com.example.youxin.network.model.response.GetFriendListResp
import com.example.youxin.utils.ContactGroupUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.map

class ContactRepository @Inject constructor(
    private val contactDao: ContactDao,
    private val applyDao: ApplyDao,
    private val socialApi: SocialApi,
    private val dataStoreManager: DataStoreManager,
    private val database: AppDatabase
) {
    private var cachedUserId: String? = null
    private suspend fun getUserId(): String {
        if (cachedUserId == null) {
            cachedUserId = dataStoreManager.getUserId()
                ?: throw IllegalStateException("用户未登录")
        }
        return cachedUserId!!
    }
    // 申请添加朋友
    suspend fun addApplyFriend(targetId: String, greetMsg: String): Boolean {
        val result = socialApi.applyFriend(getUserId(), targetId, greetMsg)
        return result != null
    }
    // 通过朋友验证
    suspend fun handleApply(
        applicantId: String,
        applicantNickname: String,
        applicantAvatar: String,
        applicantSex: Byte,
        applicantRemark: String, // 备注
        myId: String,
        isApproved: Boolean, // 是否通过
    ) {
        socialApi.handleApply(applicantId, myId, isApproved)
        if (isApproved) {
            contactDao.saveContact(
                ContactEntity(
                    applicantId,
                    applicantNickname,
                    applicantAvatar,
                    applicantSex,
                    FriendStatusEntity(false, false, false, applicantRemark)
                )
            )
        }
    }

    suspend fun deleteFriend(myId: String, targetId: String) {
        socialApi.deleteFriend(myId, targetId)
        contactDao.deleteContactById(targetId)
    }

    suspend fun findUser(phone: String?, name: String?, ids: String?): List<ContactEntity> {
        val response = socialApi.findUser(phone ?: "null", name ?: "null", ids ?: "null")
        if (response == null) {
            throw (Exception("查找用户失败"))
            Log.e("myTag", "查找用户失败")
        }
        val userList = response.infos.map {
            ContactEntity(
                it.id,
                it.nickname,
                it.avatar,
                it.sex.toByte(),
                FriendStatusEntity(false, false, false, "")
            )
        }
        return userList
    }

    val currentFriendListFlow = flow {
        // 先发射本地缓存数据，保证UI快速响应
        val localContacts = contactDao.getAllContacts().sortedBy { it.nickName }
        emit(localContacts)
        // 同步数据
        val isSyncSuccess = syncContacts()
        if (isSyncSuccess) {
            val syncContacts = contactDao.getAllContacts().sortedBy { it.nickName }
            emit(syncContacts)
        } else {
            // throw Exception("同步联系人失败")
            Log.e("myTag", "同步联系人失败")
        }
    }
    val currentApplyListFlow = flow {
        val localApplies = applyDao.getApplyList()
        emit(localApplies)

        val isSyncSuccess = syncApplies()
        if (isSyncSuccess) {
            emit(applyDao.getApplyList())
        } else {
            Log.e("myTag", "同步好友请求列表失败")
        }
    }

    suspend fun logout() {
        contactDao.deleteAllContacts()
    }

    suspend fun syncApplies(): Boolean = withContext(Dispatchers.IO) {
        val remoteApplies = socialApi.getApplyList(getUserId())
        val localApplies = applyDao.getApplyList()
        val localApplyMap = localApplies.associateBy { it.userId }
        if (remoteApplies == null) {
            return@withContext false
        }
        val appliesToAdd = remoteApplies.list.filter { item ->
            val localItem = localApplyMap[item.userId]
            localItem == null || localItem.status != item.status
        }
        appliesToAdd.forEach { item ->
            applyDao.saveApply(
                ApplyEntity(
                    item.userId,
                    item.avatarUrl,
                    item.gender,
                    item.greetMsg,
                    item.nickname,
                    item.status
                )
            )
        }

        true
    }

    // 同步本地数据库
    suspend fun syncContacts(): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = getUserId()
            val friendListResp: GetFriendListResp? = socialApi.getFriendList(userId)

            if (friendListResp?.friendList.isNullOrEmpty()) {
                contactDao.deleteAllContacts()
                return@withContext true
            }

            val serverFriends = friendListResp.friendList
            val localContacts = contactDao.getAllContacts()
            val localContactMap = localContacts.associateBy { it.id }

            val serverContacts = serverFriends.map { convertToContactEntity(it) }
            val serverContactIds = serverContacts.map { it.id }.toSet()

            // 确定需要删除的本地联系人
            val localIdsToDelete = localContacts
                .filter { !serverContactIds.contains(it.id) }
                .map { it.id }
            // 确定需要添加或更新的联系人
            val contactsToAddOrUpdate = serverContacts.filter { serverContact ->
                val localContact = localContactMap[serverContact.id]
                localContact == null || isContactChanged(localContact, serverContact)
            }
            // 使用数据库实例调用withTransaction
            database.withTransaction {
                // 批量删除
                localIdsToDelete.forEach { contactDao.deleteContactById(it) }
                // 批量添加或更新
                contactsToAddOrUpdate.forEach { contactDao.saveContact(it) }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 辅助函数 将Friend转换成ContactEntity
    private fun convertToContactEntity(friend: Friend): ContactEntity {
        return ContactEntity(
            id = friend.userId,
            nickName = friend.nickname,
            avatar = friend.avatarUrl,
            sex = friend.gender.toByte(),
            states = FriendStatusEntity(
                isMuted = friend.status.isMuted,
                isTopped = friend.status.isTopped,
                isBlocked = friend.status.isBlocked,
                remark = friend.status.remark
            )
        )
    }

    // 辅助函数 判断联系人信息是否发生改变
    private fun isContactChanged(local: ContactEntity, server: ContactEntity): Boolean {
        return local.nickName != server.nickName ||
                local.avatar != server.avatar ||
                local.sex != server.sex ||
                local.states != server.states
    }
}