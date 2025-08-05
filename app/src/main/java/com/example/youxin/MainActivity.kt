package com.example.youxin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.youxin.ui.navigation.RootNavigation
import com.example.youxin.ui.theme.YouxinTheme
import com.example.youxin.ui.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val mainViewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YouxinTheme {
                RootNavigation()
            }
        }
    }
}
