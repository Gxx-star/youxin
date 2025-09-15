package com.example.youxin.network

import android.R.id.message
import android.util.Log
import com.example.youxin.data.db.dao.ChatDao
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.data.db.entity.ConversationEntity
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.data.repository.UserRepository
import com.example.youxin.di.DataStoreManager
import com.example.youxin.network.info.MessageFrame
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.java

@Singleton
class ChatWebSocket @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val currentUserRepository: UserRepository,
    private val dataStoreManager: DataStoreManager,
    private val chatDao: ChatDao
) {

    val gson: Gson = Gson()
    val currentUserId = MutableStateFlow<String?>(null)
    var currentUser = MutableStateFlow<CurrentUserEntity?>(null)
    private var webSocket: WebSocket? = null
    private val _receivedMessages = MutableSharedFlow<MessageFrame>()
    val receivedMessages: SharedFlow<MessageFrame> = _receivedMessages

    private val _connectionState = MutableSharedFlow<Boolean>()
    val connectionState: SharedFlow<Boolean> = _connectionState

    // 重连次数计数器
    private var reconnectCount = 0

    init {
        CoroutineScope(Dispatchers.Main).launch {
            currentUserRepository.observeCurrentUser().collect {
                if (it != null) {
                    currentUser.value = it
                    connect()
                    Log.d("myTag", "收集成功")
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            dataStoreManager.getUserIdFlow.collect {
                currentUserId.value = it
            }
        }
    }

    fun connect() {
        if (currentUser.value == null) return
        if (isConnected()) return // 避免重复连接
        val request = Request.Builder()
            .url("ws://114.215.194.88:9080/ws")
            .header("userId", currentUser.value!!.id)
            .header("Authorization", currentUser.value!!.token)
            .build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                // 连接成功：重置重连计数，发送连接状态
                Log.d("myTag", "连接成功")
                reconnectCount = 0
                CoroutineScope(Dispatchers.Main).launch {
                    _connectionState.emit(true)
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("myTag", "收到消息：$text")
                val message = gson.fromJson(text, MessageFrame::class.java)
                CoroutineScope(Dispatchers.Main).launch {
                    _receivedMessages.emit(message)
                }
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: okhttp3.Response?
            ) {
                Log.d("myTag", "连接失败")
                super.onFailure(webSocket, t, response)
                t.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    _connectionState.emit(false)
                }
                this@ChatWebSocket.webSocket = null  // 置空
                reconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                CoroutineScope(Dispatchers.Main).launch {
                    _connectionState.emit(false)
                }
            }
        })
    }

    private fun reconnect() {
        if (currentUser.value == null) return
        reconnectCount++
        val delay = (1000L * (1 shl reconnectCount)).coerceAtMost(10000L) // 最大10秒
        CoroutineScope(Dispatchers.Main).launch {
            delay(delay)
            connect()
        }
    }

    // 检查是否已连接
    private fun isConnected(): Boolean {
        return webSocket != null
    }
    // 发送消息
    fun sendMessage(message: MessageFrame) {
        Log.d("myTag", "发送的消息：${message.toString()}")
        val json = gson.toJson(message)
        webSocket?.send(json)
        Log.d("myTag", "发送成功")
    }

    // 断开连接
    fun disconnect() {
        webSocket?.close(1000, "正常关闭")
        Log.d("myTag", "关闭成功")
    }
}
