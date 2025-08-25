package com.example.youxin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.youxin.data.db.dao.ApplyDao
import com.example.youxin.data.db.dao.ContactDao
import com.example.youxin.data.db.dao.CurrentUserDao
import com.example.youxin.data.db.entity.ApplyEntity
import com.example.youxin.data.db.entity.ContactEntity
import com.example.youxin.data.db.entity.CurrentUserEntity

/**
 * 数据库实例
 * @TODO 定义数据库总入口、关联所有实体和DAO
 */
@Database(
    version = 2,
    entities = [CurrentUserEntity::class, ContactEntity::class, ApplyEntity::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currentUserDao(): CurrentUserDao
    abstract fun contactDao(): ContactDao
    abstract fun applyDao(): ApplyDao
}