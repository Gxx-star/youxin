package com.example.youxin.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.youxin.data.db.entity.CurrentUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //覆盖旧数据
    suspend fun saveCurrentUser(user: CurrentUserEntity)

    @Query("SELECT * FROM current_user LIMIT 1 ")
    suspend fun getCurrentUser(): CurrentUserEntity?

    @Query("SELECT * FROM current_user LIMIT 1 ")
    fun observeCurrentUser(): Flow<CurrentUserEntity?> // 监听数据库，始终是最新的数据

    @Query("DELETE FROM current_user")
    suspend fun deleteCurrentUser()

    @Query("UPDATE current_user SET avatar = :newAvatar,nickName = :newNickName, sex = :newSex")
    suspend fun updateCurrentUser(newAvatar: String, newNickName: String, newSex: Byte)

    @Query("UPDATE current_user SET token = :newToken")
    suspend fun updateToken(newToken: String)

    @Query("UPDATE current_user SET isLogin = :isLogin")
    suspend fun updateIsLogin(isLogin: Boolean)
}