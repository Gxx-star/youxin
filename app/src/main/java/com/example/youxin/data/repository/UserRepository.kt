package com.example.youxin.data.repository

import android.util.Log
import androidx.datastore.dataStore
import com.example.youxin.data.db.dao.CurrentUserDao
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.di.DataStoreManager
import com.example.youxin.network.api.UserApi
import com.example.youxin.utils.KeyStoreUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用户的数据统一管理中心
 */
class UserRepository @Inject constructor(
    private val currentUserDao: CurrentUserDao,
    private val userApi: UserApi,
    private val dataStoreManager: DataStoreManager
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
                sex = sex,
                token = registerResp.token,
                avatar = avatar,
                isLogin = false
            )
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
            dataStoreManager.saveUserId(user?.id.toString())
            return CurrentUserEntity(
                id = 1,
                phone = phone,
                nickName = user?.nickname,
                sex = user?.sex,
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

    // 修改个人信息
    suspend fun updateUserInfo(
        nickname: String,
        sex: Byte,
        avatar: String
    ) {
        val currentUser = currentUserDao.getCurrentUser()
        val token = currentUser?.token
        Log.d("myTag", token.toString())
        if (token == null) {
            throw (Exception("用户未登录"))
        }
        val response = userApi.updateUserInfo(token, nickname, sex, avatar)
        if (response == true) {
            currentUserDao.updateCurrentUser(avatar, nickname, sex)
        }
    }

    // 退出登录
    suspend fun logout() {
        currentUserDao.updateIsLogin(false)
    }

    suspend fun switchUser() {
        currentUserDao.deleteCurrentUser()
    }

    // 观察当前用户
    fun observeCurrentUser(): Flow<CurrentUserEntity?> = currentUserDao.observeCurrentUser()
}