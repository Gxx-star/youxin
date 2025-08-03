package com.example.youxin.ui.screen.login

import android.R.attr.contentDescription
import android.R.attr.onClick
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youxin.R
import com.example.youxin.ui.component.Loading
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    registerViewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by registerViewModel.state.collectAsState()
    // 注册成功后导航到登录页
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.popBackStack() // 返回登录页
        }
    }
    // 相册选择launcher
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // 更新头像
        uri?.let {
            registerViewModel.updateAvatar(it.toString())
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(ScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部返回按钮
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "返回",
                )
            }
        }
        Text(
            text = "手机号注册",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(32.dp)
        )
        // 头像选择
        AsyncImage(
            model = uiState.avatar,
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(16.dp)
                .clickable(onClick = {
                    // 选择头像并更新
                    pickImageLauncher.launch("image/*")
                })
        )
        // 表单输入区域
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 昵称输入框
            OutlinedTextField(
                value = uiState.nickname,
                onValueChange = { registerViewModel.updateNickname(it) },
                singleLine = true,
                placeholder = { Text("请输入您的昵称，最多10个字符") },
                modifier = Modifier.fillMaxWidth()
            )
            // 手机号输入框
            OutlinedTextField(
                value = uiState.phone,
                onValueChange = {
                    registerViewModel.updatePhone(it)
                },
                singleLine = true,
                placeholder = { Text("请输入手机号") },
                modifier = Modifier.fillMaxWidth()
            )
            // 密码输入框
            OutlinedTextField(
                value = uiState.password,
                onValueChange = {
                    registerViewModel.updatePassword(it)
                },
                singleLine = true,
                placeholder = { Text("请输入密码") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        // 性别选择
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "性别",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Row {
                    RadioButton(
                        selected = uiState.sex == 0.toByte(),
                        onClick = {
                            registerViewModel.updateSex(0)
                        }
                    )
                    Text(text = "未知")
                }
                Row {
                    RadioButton(
                        selected = uiState.sex == 1.toByte(),
                        onClick = {
                            registerViewModel.updateSex(1)
                        }
                    )
                    Text(text = "男")
                }
                Row {
                    RadioButton(
                        selected = uiState.sex == 2.toByte(),
                        onClick = {
                            registerViewModel.updateSex(2)
                        }
                    )
                    Text(text = "女")
                }
            }
        }
        // 提交按钮
        Button(
            onClick = {
                registerViewModel.register()
            },
            modifier = Modifier
                .width(100.dp)
                .height(50.dp),
            enabled = !uiState.isLoading,
        ) {
            if (uiState.isLoading) {
                Loading()
            } else {
                Text(text = "欢迎加入", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

}