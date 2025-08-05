package com.example.youxin.data.repository

import android.util.Log
import com.example.youxin.data.db.dao.CurrentUserDao
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.network.api.UserApi
import com.example.youxin.utils.KeyStoreUtils
import kotlinx.coroutines.flow.Flow
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
                id = 1,
                phone = phone,
                nickName = nickname,
                token = registerResp.token,
                avatar = avatar,
                isLogin = false
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
        password: String
    ): CurrentUserEntity? {
        val loginResp = userApi.login(
            phone = phone,
            password = password
        )
        if (loginResp != null) {
            val user = userApi.getUserInfo(loginResp.token)?.info
            return CurrentUserEntity(
                id = 1,
                phone = phone,
                nickName = user?.nickname,
                token = loginResp.token,
                avatar = user?.avatar,
                isLogin = true
            ).also {
                currentUserDao.saveCurrentUser(it)
            }
        } else {
            return null
        }
    }

    // 退出登录
    suspend fun logout() {
        currentUserDao.updateIsLogin(false)
    }
    suspend fun switchUser(){
        currentUserDao.deleteCurrentUser()
    }

    // 观察当前用户
    fun observeCurrentUser(): Flow<CurrentUserEntity?> = currentUserDao.observeCurrentUser()
}