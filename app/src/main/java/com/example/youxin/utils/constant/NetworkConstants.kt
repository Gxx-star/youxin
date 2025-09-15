package com.example.youxin.utils.constant

/**
 * 配置网络常量
 */
object NetworkConstants {
    const val BASE_URL = "http://114.215.194.88:9080/"
    const val CACHE_SIZE = (10 * 1024 * 1024).toLong() // 缓存大小 10M
    const val CONNECT_TIMEOUT = 15L // 连接超时时间 15s
    const val READ_TIMEOUT = 15L // 读取超时时间 15s
    // 用户模块的uri
    object User {
        const val LOGIN = "v1/user/login"
        const val REGISTER = "v1/user/register"
        const val GET_USER_INFO = "v1/user/userinfo"
        const val UPDATE_USER_INFO = "v1/user/update"
        const val FIND_USER = "v1/user/findUser"
    }
    // 社交模块的uri
    object Social {
        const val APPLY_FRIEND = "v1/social/firend/applyFriend"
        const val HANDLE_APPLY = "v1/social/firend/handleFriendApply"
        const val DELETE_FRIEND = "v1/social/firend/deleteFriend"
        const val GET_FRIEND_LIST = "v1/social/firend/getFriendList"
        const val GET_APPLY_LIST = "v1/social/firend/getHandleFriendApplyList"
        const val UPDATE_FRIEND_STATUS = "v1/social/firend/updateFriendStatus"
    }
}