package com.example.youxin.data.repository

import com.example.youxin.data.db.dao.CurrentUserDao
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.network.api.UserApi
import com.example.youxin.utils.KeyStoreUtils
import javax.inject.Inject

/**
 * 用户的数据统一管理中心
 */
class UserRepository @Inject constructor(
    private val currentUserDao: CurrentUserDao,
    private val userApi: UserApi
) {
    // 注册
    suspend fun register(
        phone: String,
        password: String,
        nickname: String,
        sex: Byte,
        avatar: String
    ): CurrentUserEntity? {
        val registerResp = userApi.register(phone, password, nickname, sex, avatar)
        if (registerResp != null) {
            return CurrentUserEntity(
                id = null,
                phone = phone,
                nickName = nickname,
                token = registerResp.token,
                avatar = avatar,
                isLogin = true,
            ).also {
                currentUserDao.saveCurrentUser(it)
            }
        } else {
            return null
        }
    }

    // 登录
    suspend fun login(
        phone: String,
        password: String,
        isRememberPassword: Boolean
    ): CurrentUserEntity? {
        val loginResp = userApi.login(
            phone = phone,
            password = password
        )
        if (loginResp != null) {
            val encryptedPassword = if (isRememberPassword) {
                KeyStoreUtils.encrypt(password)
            } else {
                null
            }
            val user = userApi.getUserInfo(loginResp.token)?.info
            return CurrentUserEntity(
                id = null,
                phone = phone,
                nickName = user?.nickname,
                token = loginResp.token,
                avatar = user?.avatar,
            ).also {
                currentUserDao.saveCurrentUser(it)
            }
        } else {
            return null
        }
    }

    // 获取当前用户
    suspend fun getCurrentUser(): CurrentUserEntity? {
        return currentUserDao.getCurrentUser()
    }

    // 退出登录
    suspend fun logout() {
        currentUserDao.deleteCurrentUser()
    }
}