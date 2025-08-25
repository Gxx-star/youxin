package com.example.youxin.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController

@SuppressLint("RestrictedApi")
@Composable
fun YouxinScreen(
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    Text(text = "ChatScreen")
}