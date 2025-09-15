package com.example.youxin.data.db

import androidx.room.TypeConverter
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.data.db.entity.FriendStatusEntity
import com.google.gson.Gson

/**
 * 把好友状态实体类转换成json字符串
 */
class Converters {
    @TypeConverter
    fun fromFriendStatus(status: FriendStatusEntity): String {
        return Gson().toJson(status)
    }

    @TypeConverter
    fun toFriendStatus(json: String): FriendStatusEntity {
        return Gson().fromJson(json, FriendStatusEntity::class.java)
    }

    @TypeConverter
    fun fromChatLog(chatLog: ChatLogEntity): String {
        return Gson().toJson(chatLog)
    }

    @TypeConverter
    fun toChatLog(json: String): ChatLogEntity {
        return Gson().fromJson(json, ChatLogEntity::class.java)
    }
}
