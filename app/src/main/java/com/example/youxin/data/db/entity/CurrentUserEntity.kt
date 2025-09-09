package com.example.youxin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.youxin.utils.constant.DbConstants

@Entity(tableName = "current_user")
data class CurrentUserEntity(
    @PrimaryKey
    val id: String, // 用户本地id
    val phone: String, // 手机号
    val nickName: String?, // 昵称
    val token: String, // 登录令牌
    val avatar: String?, // 头像uri
    val sex: Byte? = 0,
    val isLogin: Boolean = true, // 是否处于登录状态
)
