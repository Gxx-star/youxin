package com.example.youxin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.youxin.utils.constant.DbConstants

@Entity(tableName = "current_user")
data class CurrentUserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String, // 用户id
    val phone: String, // 手机号
    val nickName: String, // 昵称
    val token: String, // 登录令牌
    val avatar: String, // 头像uri
    val isLogin: Boolean = true, // 是否处于登录状态
    val savedPassword:String?, // 保存的密码(加密后)
    val isRememberPassword:Boolean = false // 是否记住密码
)
