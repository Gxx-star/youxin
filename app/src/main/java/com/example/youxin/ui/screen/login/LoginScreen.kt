package com.example.youxin.ui.screen.login

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.LoginViewModel
import com.example.youxin.utils.constant.NavConstants
import com.example.youxin.utils.constant.NavConstants.LoginRoutes
import com.example.youxin.utils.constant.NavConstants.MainRoutes
import com.example.youxin.utils.constant.NavConstants.RootRoutes

@Composable
fun LoginScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val currentUser by appViewModel.currentUser.collectAsStateWithLifecycle()
    // 监听currentUser整体变化
    LaunchedEffect(currentUser) {
        when {
            // 已登录：直接进入主页面
            currentUser?.isLogin == true -> {
                navController.navigate(RootRoutes.MAIN_GRAPH) {
                    popUpTo(RootRoutes.LOGIN_GRAPH) {
                        inclusive = true
                    }
                }
            }
            // 未登录但有信息：进入快捷登录页面
            currentUser != null && currentUser?.isLogin == false -> {
                navController.navigate(LoginRoutes.QUICK_LOGIN_SCREEN) {
                    launchSingleTop = true
                }
            }
            // 无信息：进入登录页面
            currentUser == null -> {
                navController.navigate(LoginRoutes.FULL_LOGIN_SCREEN) {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun QuickLoginScreen(

) {
    Text(text = "快速登录")
}

@Composable
fun FullLoginScreen(

) {
    Text(text = "完整登录")
}
