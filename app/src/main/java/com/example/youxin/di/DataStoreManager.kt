package com.example.youxin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val USER_ID = stringPreferencesKey("user_id")
    }

    // 保存用户id
    suspend fun saveUserId(userId: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[USER_ID] = userId
            }
        }
    }

    val getUserIdFlow: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_ID]
        }
    suspend fun getUserId(): String?{
        return getUserIdFlow.first()
    }

    // 清空所有数据
    suspend fun clearAllData() {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { it.clear() }
        }
    }
}