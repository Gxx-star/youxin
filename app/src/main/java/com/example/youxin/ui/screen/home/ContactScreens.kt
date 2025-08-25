package com.example.youxin.ui.screen.home

import android.R.attr.onClick
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.example.youxin.R
import com.example.youxin.data.db.entity.ApplyEntity
import com.example.youxin.network.model.response.Apply
import com.example.youxin.ui.component.MyTopBar
import com.example.youxin.ui.theme.WechatGray1
import com.example.youxin.ui.theme.WechatGray4
import com.example.youxin.ui.theme.WechatGreen
import com.example.youxin.ui.theme.WechatLightGreen
import com.example.youxin.ui.theme.White
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.ContactViewModel
import com.example.youxin.utils.constant.NavConstants
import com.example.youxin.utils.constant.NavConstants.MainRoutes.ContactRoutes
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun NewFriendScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    contactViewModel: ContactViewModel
) {
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
@Composable
fun VerifiedFriendScreen(
    navController: NavController,
    contactViewModel: ContactViewModel,
    apply: ApplyEntity
) {
    Text(text = apply.nickname)
    Log.d("myTag", apply.toString())
}

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