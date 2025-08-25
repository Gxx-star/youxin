package com.example.youxin.di

import com.example.youxin.network.service.SocialService
import com.example.youxin.network.service.UserService
import com.example.youxin.utils.constant.NetworkConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 网络相关依赖，提供Retrofit、OkHttp、ApiService实例
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // 提供OkHttpClient实例用于依赖注入
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NetworkConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkConstants.READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    // 提供Retrofit实例用于依赖注入
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 提供Service实例用于依赖注入
    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideSocialService(retrofit: Retrofit): SocialService {
        return retrofit.create(SocialService::class.java)
    }
}