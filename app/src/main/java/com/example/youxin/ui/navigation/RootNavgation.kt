package com.example.youxin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.youxin.ui.screen.home.YouxinScreen
import com.example.youxin.ui.screen.login.FullLoginScreen
import com.example.youxin.ui.screen.login.LoginScreen
import com.example.youxin.ui.screen.login.QuickLoginScreen
import com.example.youxin.ui.screen.login.RegisterScreen
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.LoginViewModel
import com.example.youxin.utils.constant.NavConstants
import com.example.youxin.utils.constant.NavConstants.RootRoutes

@Composable
fun RootNavgation() {
    // 管理自动注入通过构造函数给每一个页面
    val navController = rememberNavController()
    val appViewModel: AppViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = RootRoutes.LOGIN_GRAPH
    ) {
        navigation(
            route = RootRoutes.LOGIN_GRAPH,
            startDestination = NavConstants.LoginRoutes.LOGIN_SCREEN
        ) {
            composable(NavConstants.LoginRoutes.LOGIN_SCREEN) {
                LoginScreen(navController, loginViewModel)
            }
            composable(NavConstants.LoginRoutes.QUICK_LOGIN_SCREEN) {
                QuickLoginScreen(navController, loginViewModel)
            }
            composable(NavConstants.LoginRoutes.FULL_LOGIN_SCREEN){
                FullLoginScreen(navController,loginViewModel)
            }
            composable(NavConstants.LoginRoutes.REGISTER_SCREEN) {
                RegisterScreen(navController, appViewModel)
            }
        }
        navigation(
            route = RootRoutes.MAIN_GRAPH,
            startDestination = NavConstants.MainRoutes.YOUXIN_SCREEN
        ) {
            composable(NavConstants.MainRoutes.YOUXIN_SCREEN) {
                YouxinScreen(navController, appViewModel)
            }
            composable(NavConstants.MainRoutes.CONTACT_SCREEN) {

            }
            composable(NavConstants.MainRoutes.DISCOVER_SCREEN) {

            }
            composable(NavConstants.MainRoutes.ME_SCREEN) {

            }
        }
    }
}