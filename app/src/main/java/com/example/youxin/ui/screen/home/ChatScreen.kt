package com.example.youxin.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.youxin.ui.component.AppButton
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.utils.constant.NavConstants
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    navController: NavController,
    appViewModel: AppViewModel
) {
    val scope = rememberCoroutineScope()
    AppButton(
        text = "退出登录",
        onClick = {
            scope.launch {
                appViewModel.logout()
                navController.navigate(NavConstants.RootRoutes.LOGIN_GRAPH) {
                    // 避免重复添加登录页面
                    launchSingleTop = true
                }
            }
        }
    )
}