package com.example.youxin.ui.screen.login

import android.R.attr.contentDescription
import android.R.attr.onClick
import android.R.attr.text
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youxin.R
import com.example.youxin.ui.component.AppButton
import com.example.youxin.ui.component.Loading
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.RegisterViewModel
import com.example.youxin.utils.OssUploader
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.youxin.MyApplication
import com.example.youxin.ui.theme.WechatGray2
import com.example.youxin.ui.theme.WechatGray4
import kotlinx.coroutines.flow.map

@Composable
fun RegisterScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    registerViewModel: RegisterViewModel = hiltViewModel(),
) {
    val errorMsg by registerViewModel.state.map {
        it.errorMessage
    }.collectAsStateWithLifecycle(null)
    LaunchedEffect(errorMsg) {
        if (errorMsg != null) {
            Toast.makeText(MyApplication.getContext(), errorMsg, Toast.LENGTH_SHORT).show()
            registerViewModel.clearErrorMessage()
        }
    }
    val context = LocalContext.current
    // 密码可见性设置
    var isPasswordVisible by remember { mutableStateOf(false) }
    val uiState by registerViewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    // 注册成功后导航到登录页
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.popBackStack() // 返回登录页
        }
    }
    // 相册选择launcher
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        // 更新头像
        uri?.let {
            scope.launch {
                val uri = OssUploader.uploadFile(
                    registerViewModel.saveAvatarToPrivateDir(context, it)?.toUri()
                )
                uri.let { uriString ->
                    registerViewModel.updateAvatar(uriString)
                }
            }
        }
    }
    Box (
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
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
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(32.dp)
            )
            // 头像选择
            AsyncImage(
                model = uiState.avatar,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .width(100.dp)
                    .height(100.dp)
                    .clickable(onClick = {
                        // 选择头像并更新
                        val request = PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                        pickImageLauncher.launch(request)
                    }),
                contentScale = ContentScale.Crop
            )
            // 表单输入区域
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 昵称输入框
                OutlinedTextField(
                    value = uiState.nickname,
                    onValueChange = { registerViewModel.updateNickname(it) },
                    singleLine = true,
                    placeholder = { Text("请输入您的昵称，最多10个字符") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (uiState.nickname.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    registerViewModel.updateNickname("")
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.clear),
                                    contentDescription = "清空"
                                )
                            }
                        }
                    }
                )
                // 手机号输入框
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = {
                        registerViewModel.updatePhone(it)
                    },
                    singleLine = true,
                    placeholder = { Text("请输入手机号") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (uiState.phone.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    registerViewModel.updatePhone("")
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.clear),
                                    contentDescription = "清空"
                                )
                            }
                        }
                    }
                )
                // 密码输入框
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = {
                        registerViewModel.updatePassword(it)
                    },
                    singleLine = true,
                    placeholder = { Text("请输入密码") },
                    visualTransformation = if (isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }) {
                            Icon(
                                painter = painterResource(id = if (isPasswordVisible) R.drawable.visible else R.drawable.invisible),
                                contentDescription = null
                            )
                        }
                    }
                )
                Text(
                    text = "密码必须由6-16位字符组成并且至少包含大小写字母和字符各一个",
                    fontSize = 10.sp,
                    color = WechatGray4
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
                        Text(
                            text = "未知",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    Row {
                        RadioButton(
                            selected = uiState.sex == 1.toByte(),
                            onClick = {
                                registerViewModel.updateSex(1)
                            }
                        )
                        Text(
                            text = "男",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    Row {
                        RadioButton(
                            selected = uiState.sex == 2.toByte(),
                            onClick = {
                                registerViewModel.updateSex(2)
                            }
                        )
                        Text(
                            text = "女",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
            // 提交按钮
            AppButton(
                text = "欢迎加入",
                enabled = !uiState.isLoading,
                onClick = {
                    registerViewModel.register()
                },
                isLoading = uiState.isLoading,
                modifier = Modifier
                    .width(120.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        // 底部文字
        Text(
            text = "注册即表示同意《友信用户协议》",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(bottom = 30.dp,top = 10.dp)
                .align(Alignment.BottomCenter)
        )
    }

}