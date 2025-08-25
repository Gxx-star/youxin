package com.example.youxin.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.youxin.R
import com.example.youxin.ui.component.MenuItem

@SuppressLint("RestrictedApi")
@Composable
fun DiscoverScreen(navController: NavController) {
    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MenuItem(
                icon = R.drawable.discover_moments,
                title = "朋友圈",
                currentValue = null,
                displayArrow = true,
                onClick = {})
        }
    }
}