package com.example.youxin.network.api

import com.example.youxin.network.model.ApiResponse
import com.example.youxin.network.model.request.ApplyFriendReq
import com.example.youxin.network.model.request.HandleApplyReq
import com.example.youxin.network.model.request.Status
import com.example.youxin.network.model.request.UpdateFriendStatusReq
import com.example.youxin.network.model.response.ApplyFriendResp
import com.example.youxin.network.model.response.FindUserResp
import com.example.youxin.network.model.response.GetApplyListResp
import com.example.youxin.network.model.response.GetFriendListResp
import com.example.youxin.network.service.SocialService
import javax.inject.Inject

class SocialApi @Inject constructor(
    private val socialService: SocialService
) {
    suspend fun applyFriend(
        applicantId: String,
        targetId: String,
        greetMsg: String
    ): ApplyFriendResp? {
        val request = ApplyFriendReq(applicantId, greetMsg, targetId)
        val response = socialService.applyFriend(request)
        return if (response.msg != "success") {
            null
        } else {
            response.data
        }
    }

    suspend fun handleApply(
        applicantId: String,
        targetId: String,
        isApproved: Boolean
    ) {
        val request = HandleApplyReq(applicantId, isApproved, targetId)
        socialService.handleApply(request)
    }

    suspend fun deleteFriend(fromUid: String, toUid: String) {
        socialService.deleteFriend(fromUid, toUid)
    }

    suspend fun getFriendList(userId: String): GetFriendListResp? {
        val response = socialService.getFriendList(userId)
        return if (response.code == 200) {
            response.data!!
        } else {
            null
        }
    }

    suspend fun getApplyList(targetId: String): GetApplyListResp? {
        val response = socialService.getApplyList(targetId)
        return if (response.code == 200) {
            response.data!!
        } else {
            null
        }
    }

    suspend fun findUser(phone: String, name: String, ids: String): FindUserResp? {
        val response = socialService.findUser(phone, name, ids)
        return if (response.code == 200) {
            response.data!!
        } else {
            null
        }
    }

    suspend fun updateFriendStatus(
        fromUid: String,
        toUid: String,
        isMuted: Boolean,
        isTopped: Boolean,
        isBlocked: Boolean,
        remark: String?
    ) {
        val request =
            UpdateFriendStatusReq(fromUid, toUid, Status(isBlocked, isMuted, isTopped, remark))
        socialService.updateFriendStatus(request)
    }
}