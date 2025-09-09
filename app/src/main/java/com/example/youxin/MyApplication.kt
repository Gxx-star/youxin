package com.example.youxin

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(){
    companion object {
        // 全局Context实例（Application级别，生命周期与应用一致）
        @Volatile
        private lateinit var INSTANCE: MyApplication

        // 获取全局Context的方法
        fun getContext(): Context {
            // 双重校验锁确保线程安全（防止初始化时的并发问题）
            if (!::INSTANCE.isInitialized) {
                synchronized(MyApplication::class.java) {
                    if (!::INSTANCE.isInitialized) {
                        // 理论上不会走到这里，因为Application实例由系统创建
                        throw IllegalStateException("MyApplication尚未初始化，请检查Manifest配置")
                    }
                }
            }
            return INSTANCE.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化INSTANCE为当前Application实例
        INSTANCE = this
    }
}