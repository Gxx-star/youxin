package com.example.youxin.network.service

import com.example.youxin.network.model.ApiResponse
import com.example.youxin.network.model.request.ApplyFriendReq
import com.example.youxin.network.model.request.HandleApplyReq
import com.example.youxin.network.model.request.UpdateFriendStatusReq
import com.example.youxin.network.model.response.ApplyFriendResp
import com.example.youxin.network.model.response.FindUserResp
import com.example.youxin.network.model.response.GetApplyListResp
import com.example.youxin.network.model.response.GetFriendListResp
import com.example.youxin.network.model.response.UserinfoResp
import com.example.youxin.utils.constant.NetworkConstants
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface SocialService {
    @POST(NetworkConstants.Social.APPLY_FRIEND)
    suspend fun applyFriend(@Body request: ApplyFriendReq): ApiResponse<ApplyFriendResp>

    @GET(NetworkConstants.User.FIND_USER)
    suspend fun findUser(
        @Query("phone") phone: String? = "\"\"",
        @Query("name") name: String? = "\"\"",
        @Query("ids") ids: String? = "\"\""
    ): ApiResponse<FindUserResp>

    @POST(NetworkConstants.Social.HANDLE_APPLY)
    suspend fun handleApply(@Body request: HandleApplyReq)

    @DELETE(NetworkConstants.Social.DELETE_FRIEND)
    suspend fun deleteFriend(@Query("from_uid") fromUid: String, @Query("to_uid") toUid: String)

    @GET(NetworkConstants.Social.GET_FRIEND_LIST)
    suspend fun getFriendList(@Query("user_id") userId: String): ApiResponse<GetFriendListResp>

    @GET(NetworkConstants.Social.GET_APPLY_LIST)
    suspend fun getApplyList(@Query("targetId") targetId: String): ApiResponse<GetApplyListResp>

    @PUT(NetworkConstants.Social.UPDATE_FRIEND_STATUS)
    suspend fun updateFriendStatus(@Body request: UpdateFriendStatusReq)
}