package com.example.youxin.ui.screen.home

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youxin.R
import com.example.youxin.data.db.entity.ApplyEntity
import com.example.youxin.data.db.entity.ContactEntity
import com.example.youxin.ui.component.MenuItem
import com.example.youxin.ui.component.MyTopBar
import com.example.youxin.ui.theme.WechatGray1
import com.example.youxin.ui.theme.WechatGray4
import com.example.youxin.ui.theme.WechatLightBlue
import com.example.youxin.ui.theme.WechatLightGray
import com.example.youxin.ui.theme.White
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.ContactViewModel
import com.example.youxin.utils.constant.NavConstants
import com.example.youxin.utils.constant.NavConstants.MainRoutes.ContactRoutes
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch

/**
 * 新的好友页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFriendScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    contactViewModel: ContactViewModel
) {
    LaunchedEffect(Unit) {
        contactViewModel.syncWithServer()
    }
    val applyList by contactViewModel.currentApplyList.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            MyTopBar(
                title = "新的朋友",
                onClick = {
                    navController.popBackStack()
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(ContactRoutes.ADD_FRIEND_SCREEN)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_friend),
                            contentDescription = "添加好友",
                            modifier = Modifier
                                .size(20.dp)
                                .offset(y = (-5).dp)
                        )
                    }
                })
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(30.dp)
                ) {
                    Text(
                        text = "申请记录",
                        fontSize = 16.sp,
                        color = WechatGray4
                    )
                }
                LazyColumn {
                    items(applyList.size) {
                        ApplyItem(applyList[it], navController)
                    }
                }
            }
        }
    }
}

/**
 * 好友申请项
 */
@SuppressLint("RestrictedApi")
@Composable
fun ApplyItem(apply: ApplyEntity, navController: NavController) {
    Box(
        modifier = Modifier
            .padding(top = 0.5.dp)
            .fillMaxWidth()
            .height(60.dp)
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            // 头像
            AsyncImage(
                model = apply.avatarUrl,
                contentDescription = "申请人的头像",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop
            )
            // 申请人名字和打招呼信息
            Box {
                Text(
                    text = apply.nickname,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
                Text(
                    text = apply.greetMsg,
                    fontSize = 12.sp,
                    color = WechatGray4,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 20.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            // 右侧：如果已通过显示文字，否则显示按钮
            when (apply.status) {
                0 -> {
                    Button(
                        onClick = {
                            val applyGson = GsonBuilder().create().toJson(apply)
                            val encodedGson = Uri.encode(applyGson)
                            navController.navigate("${ContactRoutes.VERIFIED_FRIEND}/$encodedGson")
                        },
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .width(50.dp)
                            .height(30.dp),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 5.dp),
                        colors = buttonColors(
                            containerColor = WechatGray1,
                            contentColor = Color.Black
                        ),
                    ) {
                        Text(
                            text = "查看",
                            fontSize = 12.sp
                        )
                    }
                }

                1 -> {
                    Text(
                        text = "已通过",
                        fontSize = 12.sp,
                        color = WechatGray4
                    )
                }

                2 -> {
                    Text(
                        text = "已拒绝",
                        fontSize = 12.sp,
                        color = WechatGray4
                    )
                }
            }
        }
    }
}

/**
 * 验证好友页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun VerifiedFriendScreen(
    navController: NavController,
    contactViewModel: ContactViewModel,
    apply: ApplyEntity
) {
    val inputRemark by contactViewModel.inputRemark.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { contactViewModel.updateRemark(apply.nickname) }
    Scaffold(
        topBar = {
            MyTopBar(
                title = "通过朋友验证",
                onClick = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .background(White)
                        .padding(top = 0.5.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth(),
                ) {
                    // 资料卡片
                    Row {
                        AsyncImage(
                            model = apply.avatarUrl,
                            contentDescription = "申请人头像",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 20.dp)
                        ) {
                            Text(
                                text = apply.nickname,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.Black,
                            )
                            Text(
                                text = "友信号: ${apply.userId}",
                                fontSize = 12.sp,
                                color = WechatGray4
                            )
                            Text(
                                text = "性别: " + when (apply.gender) {
                                    0 -> "未知"
                                    1 -> "男"
                                    2 -> "女"
                                    else -> "未知"
                                },
                                fontSize = 12.sp,
                                color = WechatGray4
                            )
                        }
                    }
                    // 验证消息
                    Column(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .background(WechatGray1)
                            .border(1.dp, WechatGray4, RoundedCornerShape(5.dp))
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = apply.nickname + ':' + apply.greetMsg,
                            fontSize = 15.sp,
                            color = WechatGray4,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                        )
                    }
                }
                // 备注和按钮区域
                Column(
                    modifier = Modifier
                        .background(WechatLightGray)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "备注",
                        fontSize = 15.sp,
                        color = WechatGray4,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    TextField(
                        value = inputRemark,
                        onValueChange = {
                            contactViewModel.updateRemark(it)
                        },
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, WechatGray4, RoundedCornerShape(5.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = WechatGray4,
                            focusedContainerColor = WechatGray1,
                            unfocusedContainerColor = WechatGray1,
                        ),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .background(White)
                            .fillMaxWidth()
                            .height(50.dp)
                            .fillMaxWidth()
                            .clickable(onClick = {
                                scope.launch {
                                    // 通过申请
                                    contactViewModel.handleApply(
                                        apply.userId,
                                        apply.nickname,
                                        apply.avatarUrl,
                                        apply.gender.toByte(),
                                        inputRemark,
                                        true,
                                        apply.greetMsg
                                    )
                                }
                                navController.popBackStack()
                            })
                    ) {
                        Text(
                            text = "通过申请",
                            modifier = Modifier
                                .align(Alignment.Center),
                            color = WechatLightBlue,
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .background(White)
                            .fillMaxWidth()
                            .height(50.dp)
                            .fillMaxWidth()
                            .clickable(onClick = {
                                // 拒绝
                                scope.launch {
                                    contactViewModel.handleApply(
                                        apply.userId,
                                        apply.nickname,
                                        apply.avatarUrl,
                                        apply.gender.toByte(),
                                        inputRemark,
                                        false,
                                        apply.greetMsg
                                    )
                                }
                                navController.popBackStack()
                            })
                    ) {
                        Text(
                            text = "拒绝",
                            modifier = Modifier
                                .align(Alignment.Center),
                            color = WechatLightBlue,
                        )
                    }
                }
            }
        }
    }
}

/**
 * 添加好友页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    navController: NavController,
    contactViewModel: ContactViewModel
) {
    Scaffold(
        topBar = {
            MyTopBar(
                title = "添加好友",
                onClick = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {}

    }
}

/**
 * 好友详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    navController: NavController,
    contactViewModel: ContactViewModel,
    contact: ContactEntity
) {
    Scaffold(
        topBar = {
            MyTopBar(
                title = "",
                onClick = {
                    navController.popBackStack()
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = White
                ),
                actions = {
                    IconButton(
                        onClick = {
                            val encodedGson = Uri.encode(GsonBuilder().create().toJson(contact))
                            // 好友资料设置页面
                            navController.navigate("${NavConstants.MainRoutes.ContactRoutes.FRIEND_DATA_SETTINGS}/$encodedGson")
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "好友设置按钮"
                        )
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WechatLightGray)
            ) {
                // 资料卡
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(White)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp, start = 20.dp)
                            .fillMaxSize()
                    ) {
                        AsyncImage(
                            model = contact.avatar,
                            contentDescription = "头像",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            contentScale = ContentScale.Crop
                        )
                        // 备注(如果有)、昵称、友信号
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 20.dp)
                        ) {
                            val haveRemark =
                                contact.status.remark != null && contact.status.remark.isNotEmpty()
                            if (haveRemark) {
                                Row {
                                    Text(
                                        text = contact.status.remark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    if (contact.sex != 0.toByte()) {
                                        Icon(
                                            painter = painterResource(id = if (contact.sex == 1.toByte()) R.drawable.man else R.drawable.woman),
                                            contentDescription = "性别图标",
                                            modifier = Modifier
                                                .padding(start = 5.dp, top = 5.dp)
                                                .size(20.dp),
                                            tint = Color.Unspecified
                                        )
                                    }
                                }
                            }
                            Row {
                                Text(
                                    text = if (haveRemark) "昵称：${contact.nickName}" else contact.nickName,
                                    fontWeight = if (haveRemark) FontWeight.Normal else FontWeight.Bold,
                                    color = if (haveRemark) WechatGray4 else Black,
                                    fontSize = if (haveRemark) 15.sp else 20.sp
                                )
                                if (!haveRemark) {
                                    if (contact.sex != 0.toByte()) {
                                        Icon(
                                            painter = painterResource(id = if (contact.sex == 1.toByte()) R.drawable.man else R.drawable.woman),
                                            contentDescription = "性别图标",
                                            modifier = Modifier
                                                .padding(start = 5.dp, top = 5.dp)
                                                .size(20.dp),
                                            tint = Color.Unspecified
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "友信号: ${contact.id}",
                                fontSize = 15.sp,
                                color = WechatGray4
                            )
                        }
                    }
                }
                // 朋友圈
                MenuItem(title = "朋友圈", displayArrow = true) {
                    // 进入该联系人朋友圈
                }
                // 聊天
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .height(80.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(White)
                            .clickable(onClick = {
                                // 进入聊天页面
                            }),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.send_message),
                            contentDescription = "发送消息图标",
                            modifier = Modifier
                                .size(80.dp, 26.dp),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 0.5.dp)
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(White)
                            .clickable(onClick = {}),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.call),
                            contentDescription = "音视频通话图标",
                            modifier = Modifier
                                .size(100.dp, 26.dp),
                        )
                    }
                }
            }
        }

    }
}
/**
 * 好友设置页面
 */
@Composable
fun FriendDataSettingsScreen(
    navController: NavController,
    contactViewModel: ContactViewModel,
    contact: ContactEntity
){
    val currentSelectedContactStatus by contactViewModel.contactStatusFlow.collectAsState()
    
}
