package com.example.youxin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.youxin.data.db.dao.CurrentUserDao
import com.example.youxin.data.db.entity.CurrentUserEntity

/**
 * 数据库实例
 * @TODO 定义数据库总入口、关联所有实体和DAO
 */
@Database(
    version = 1,
    entities = [CurrentUserEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): CurrentUserDao
}