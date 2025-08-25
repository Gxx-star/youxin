package com.example.youxin.ui.screen.login

import android.R.attr.bottom
import android.R.attr.singleLine
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youxin.R
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.ui.component.AppButton
import com.example.youxin.ui.component.Loading
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.LoginViewModel
import com.example.youxin.utils.constant.NavConstants.LoginRoutes
import com.example.youxin.utils.constant.NavConstants.RootRoutes
import kotlinx.coroutines.delay
import androidx.core.net.toUri
import kotlinx.coroutines.launch
import kotlin.math.log


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val currentUser by loginViewModel.currentUser.collectAsStateWithLifecycle()
    // 每次进入登录导航时重置登录状态
    SideEffect {
        loginViewModel.resetState()
    }
    // 在刚开始启动时的缓冲，数据加载完毕后再进入导航
    val isLoading by loginViewModel.isLoading.collectAsStateWithLifecycle()
    if (isLoading) {
        Loading()
        return
    }
    // 监听currentUser整体变化
    LaunchedEffect(currentUser) {
        when {
            // 已登录：直接进入主页面
            currentUser?.isLogin == true -> {
                Log.d("myTag", "导航进入主页面")
                navController.navigate(RootRoutes.MAIN_GRAPH) {
                    popUpTo(RootRoutes.LOGIN_GRAPH){
                        inclusive = true // 导航进主页面之后清空栈
                    }
                    launchSingleTop = true
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
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val currentUser: CurrentUserEntity? by loginViewModel.currentUser.collectAsStateWithLifecycle()
    val uiState by loginViewModel.state.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    // 登录成功后跳转
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(RootRoutes.MAIN_GRAPH) {
                popUpTo(RootRoutes.LOGIN_GRAPH) {
                    inclusive = true
                }
            }
        }
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 头像区域
            AsyncImage(
                model = Uri.parse(currentUser?.avatar?:"https://i.postimg.cc/W4qrZcBH/default-avatar.png"),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(5.dp),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = currentUser?.phone.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 70.dp)
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.updatePassword(it) },
                placeholder = { Text(text = "请输入密码") },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            painter = painterResource(
                                id =
                                    if (passwordVisible)
                                        R.drawable.visible
                                    else
                                        R.drawable.invisible
                            ),
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
        }
        AppButton(
            text = "登录",
            modifier = Modifier
                .width(100.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 200.dp),
            onClick = {
                loginViewModel.login()
            }
        )
        TextButton(
            onClick = {
                scope.launch {
                    loginViewModel.switchUser()
                    navController.navigate(LoginRoutes.LOGIN_SCREEN)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            Text(text = "登录其他账号")
        }
        Text(
            text = "登录即表示同意《友信用户协议》",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        )
    }
}

@Composable
fun FullLoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val uiState by loginViewModel.state.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }
    // 登录成功后跳转
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(RootRoutes.MAIN_GRAPH) {
                popUpTo(RootRoutes.LOGIN_GRAPH) {
                    inclusive = true
                }
            }
        }
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "手机号登录",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 100.dp, bottom = 32.dp)
            )
            // 表单
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = "手机号：",
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                    OutlinedTextField(
                        value = uiState.phone,
                        onValueChange = { loginViewModel.updatePhone(it) },
                        placeholder = { Text(text = "请输入手机号") },
                        trailingIcon = {
                            if (uiState.phone.isNotEmpty()) {
                                IconButton(
                                    onClick = { loginViewModel.updatePhone("") }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.clear),
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                Row(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = "密码：",
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp)
                    )
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { loginViewModel.updatePassword(it) },
                        placeholder = { Text(text = "请输入密码") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisible) R.drawable.visible else R.drawable.invisible),
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true
                    )
                }
            }
            AppButton(
                text = "登录",
                onClick = {
                    loginViewModel.login()
                },
                modifier = Modifier.width(100.dp),
                enabled = !uiState.isLoading,
                isLoading = uiState.isLoading
            )
        }
        TextButton(
            onClick = {
                navController.navigate(LoginRoutes.REGISTER_SCREEN)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            Text(text = "还没有账号？立即注册")
        }
        // 底部文字
        Text(
            text = "登录即表示同意《友信用户协议》",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        )
    }
}
