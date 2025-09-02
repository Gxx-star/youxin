package com.example.youxin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.youxin.data.db.entity.ApplyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplyDao {
    @Query("select * from applies")
    fun getApplyListFlow(): Flow<List<ApplyEntity>>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun saveApply(apply: ApplyEntity)

    @Query("delete from applies where userId = :userId")
    suspend fun deleteApplyByUserId(userId: String)
}