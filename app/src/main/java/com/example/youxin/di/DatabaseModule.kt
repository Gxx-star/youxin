package com.example.youxin.di

import android.app.Application
import androidx.room.Room
import com.example.youxin.data.db.AppDatabase
import com.example.youxin.data.db.dao.ApplyDao
import com.example.youxin.data.db.dao.ChatDao
import com.example.youxin.data.db.dao.ContactDao
import com.example.youxin.data.db.dao.CurrentUserDao
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
        return Room.databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    // 提供数据库访问接口实例
    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): CurrentUserDao {
        return appDatabase.currentUserDao()
    }

    @Provides
    @Singleton
    fun provideContactDao(appDatabase: AppDatabase): ContactDao {
        return appDatabase.contactDao()
    }

    @Provides
    @Singleton
    fun provideApplyDao(appDatabase: AppDatabase): ApplyDao {
        return appDatabase.applyDao()
    }

    @Provides
    @Singleton
    fun provideChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }
}