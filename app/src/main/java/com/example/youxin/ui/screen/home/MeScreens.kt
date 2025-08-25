package com.example.youxin.ui.screen.home

import android.R.attr.enabled
import android.R.attr.end
import android.R.attr.fontWeight
import android.R.attr.onClick
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youxin.R
import com.example.youxin.data.db.entity.CurrentUserEntity
import com.example.youxin.ui.component.AppButton
import com.example.youxin.ui.component.MenuItem
import com.example.youxin.ui.component.MyTopBar
import com.example.youxin.ui.navigation.items
import com.example.youxin.ui.theme.WechatGray1
import com.example.youxin.ui.theme.WechatGray4
import com.example.youxin.ui.theme.WechatGreen
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.MeViewModel
import com.example.youxin.ui.viewmodel.PersonalDataViewModel
import com.example.youxin.utils.constant.NavConstants
import com.example.youxin.utils.constant.NavConstants.MainRoutes.MeRoutes
import com.google.common.collect.Multimaps.index
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 从[我的]页面打开的一些Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    rootNavController: NavController,
    appViewModel: AppViewModel
) {
    val settingItems = listOf(
        SettingsMenu.Information,
        SettingsMenu.Logout
    )
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            MyTopBar(title = "设置", onClick = { navController.popBackStack() })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
        ) {
            items(settingItems.size) { index ->
                MenuItem(
                    icon = null,
                    title = settingItems[index].title,
                    currentValue = null,
                    displayArrow = true,
                    onClick = {
                        if (settingItems[index].route != null) {
                            navController.navigate(settingItems[index].route.toString())
                        } else {
                            when (settingItems[index].title) {
                                "退出登录" -> {
                                    scope.launch {
                                        appViewModel.logout()
                                        delay(1000)
                                        rootNavController.navigate(NavConstants.RootRoutes.LOGIN_GRAPH) {
                                            popUpTo(NavConstants.RootRoutes.MAIN_GRAPH) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    personalDataViewModel: PersonalDataViewModel
) {
    val user: CurrentUserEntity? by appViewModel.currentUser.collectAsState()
    val userId by appViewModel.userIdFlow.collectAsState(null)
    val items = listOf(
        PersonalDataMenu.Avatar to user?.avatar,
        PersonalDataMenu.NickName to user?.nickName,
        PersonalDataMenu.Gender to when (user?.sex) {
            0.toByte() -> "未知"
            1.toByte() -> "男"
            2.toByte() -> "女"
            else -> "未知"
        },
        PersonalDataMenu.Phone to user?.phone,
        PersonalDataMenu.Id to userId
    )
    Scaffold(
        topBar = {
            MyTopBar(title = "个人资料", onClick = { navController.popBackStack() })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
        ) {
            items(items.size) { index ->
                when (items[index].first.title) {
                    "头像" -> {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            MenuItem(
                                icon = null,
                                title = items[index].first.title,
                                currentValue = null,
                                displayArrow = false,
                                onClick = {
                                    if (items[index].first.route != null) {
                                        navController.navigate(items[index].first.route.toString())
                                    }
                                })
                            AsyncImage(
                                model = items[index].second,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 20.dp, top = 10.dp)
                                    .size(30.dp)
                            )
                        }
                    }

                    else -> {
                        MenuItem(
                            icon = null,
                            title = items[index].first.title,
                            currentValue = items[index].second.toString(),
                            displayArrow = true,
                            onClick = {
                                if (items[index].first.route != null) {
                                    navController.navigate(items[index].first.route.toString())
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateNicknameScreen(
    navController: NavController,
    meViewModel: MeViewModel
) {
    val uiState by meViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    meViewModel.initializeMeState()
    Scaffold(
        topBar = {
            Box {
                MyTopBar(
                    title = "更改名字",
                    onClick = { navController.popBackStack() },
                    actions = {
                        Button(
                            onClick = {
                                scope.launch {
                                    if (meViewModel.updateUserInfo()) {
                                        navController.popBackStack()
                                    }
                                }
                            },
                            enabled = uiState.inputNickname.orEmpty().isNotEmpty(),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .width(55.dp)
                                .height(30.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.inputNickname.orEmpty()
                                        .isNotEmpty()
                                ) WechatGreen else WechatGray1,
                                contentColor = if (uiState.inputNickname.orEmpty()
                                        .isNotEmpty()
                                ) Color.White else Color(0xFF999999),
                            )
                        ) {
                            Text(
                                text = "保存",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            TextField(
                value = uiState.inputNickname.toString(),
                onValueChange = { meViewModel.inputNickname(it) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFADFF3F),
                    unfocusedIndicatorColor = WechatGray1,
                ),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "好名字可以让你的朋友更容易记住你",
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall,
                color = WechatGray4,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .offset(y = 70.dp)
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UpdateSexScreen(
    navController: NavController,
    meViewModel: MeViewModel
) {
    val scope = rememberCoroutineScope()
    val selectedSex by meViewModel.uiState
        .map { it.selectedSex }
        .collectAsState(null)
    meViewModel.initializeMeState()
    Scaffold(
        topBar = {
            Box {
                MyTopBar(
                    title = "设置性别",
                    onClick = {
                        navController.popBackStack()
                    },
                    actions = {
                        Button(
                            onClick = {
                                scope.launch {
                                    if (meViewModel.updateUserInfo())
                                        navController.popBackStack()
                                }
                            },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .width(55.dp)
                                .height(30.dp),
                            contentPadding = PaddingValues(4.dp, 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WechatGreen,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "完成",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            MenuItem(title = "男", onClick = {
                meViewModel.selectSex(1)
            }, displayTick = selectedSex == 1.toByte())
            MenuItem(title = "女", onClick = {
                meViewModel.selectSex(2)
            }, displayTick = selectedSex == 2.toByte())
            MenuItem(title = "保密", onClick = {
                meViewModel.selectSex(0)
            }, displayTick = selectedSex == 0.toByte())
        }
    }
}

sealed class PersonalDataMenu(val title: String, val route: String?) {
    object Avatar : PersonalDataMenu("头像", null)
    object NickName : PersonalDataMenu("昵称", MeRoutes.UPDATE_NICKNAME)
    object Gender : PersonalDataMenu("性别", MeRoutes.UPDATE_SEX)
    object Phone : PersonalDataMenu("手机号", null)
    object Id : PersonalDataMenu("账号", null)
}

sealed class SettingsMenu(val title: String, val route: String?) {
    object Information : SettingsMenu("个人资料", MeRoutes.MY_INFORMATION_SCREEN)
    object Logout : SettingsMenu("退出登录", null)
}