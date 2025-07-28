package com.example.youxin.di

import android.app.Application
import androidx.media3.database.StandaloneDatabaseProvider.DATABASE_NAME
import androidx.room.Room
import com.example.youxin.data.db.AppDatabase
import com.example.youxin.data.db.dao.CurrentUserDao
import com.example.youxin.utils.constant.DbConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库相关依赖注入模块
 * @TODO 负责提供Room数据库实例及数据访问接口，通过Hilt实现全局共享
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    const val DATABASE_NAME = "youxin_db"
    // 提供数据库实例
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java,DATABASE_NAME)
            .build()
    }
    // 提供数据库访问接口实例
    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): CurrentUserDao {
        return appDatabase.userDao()
    }
}