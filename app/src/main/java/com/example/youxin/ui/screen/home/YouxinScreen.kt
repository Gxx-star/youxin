package com.example.youxin.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.ChatViewModel

@SuppressLint("RestrictedApi")
@Composable
fun YouxinScreen(
    navController: NavController,
    chatViewModel: ChatViewModel
) {
    val scope = rememberCoroutineScope()

}