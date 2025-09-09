package com.example.youxin.ui.screen.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.ChatViewModel

@SuppressLint("RestrictedApi")
@Composable
fun YouxinScreen(
    navController: NavController,
    chatViewModel: ChatViewModel
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        chatViewModel.sendMsg(
            ChatLogEntity(
                id = "1",
                conversationId = "1",
                sendId = "1",
                recvId = "2",
                msgType = 1,
                msgContent = "hello",
                chatType = 1,
                sendTime = System.currentTimeMillis()
            )
        )
    }
}