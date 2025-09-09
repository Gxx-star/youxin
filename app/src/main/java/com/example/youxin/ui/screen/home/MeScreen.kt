package com.example.youxin.ui.screen.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.R
import com.example.youxin.ui.component.MenuItem
import com.example.youxin.ui.theme.WechatGray4
import com.example.youxin.ui.theme.WechatLightGray
import com.example.youxin.utils.constant.NavConstants.MainRoutes.MeRoutes

@SuppressLint("RestrictedApi")
@Composable
fun MeScreen(
    navController: NavController,
    appViewModel: AppViewModel
) {
    val scope = rememberCoroutineScope()
    val currentUser by appViewModel.currentUser.collectAsState()
    Log.d("myTag", currentUser?.avatar.toString())
    val items = listOf(
        MeMenu.Service,
        MeMenu.Like,
        MeMenu.CardBag,
        MeMenu.Setting
    )
    val scrollState = rememberScrollState()
    val userId = appViewModel.dataStoreManager.getUserIdFlow.collectAsState(initial = null)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WechatLightGray)
            // 添加垂直滚动支持并关联滚动状态
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            // 头像、昵称、微信号区域
            Box(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 40.dp)
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable(
                        onClick = {
                            // 个人资料
                            navController.navigate(MeRoutes.MY_INFORMATION_SCREEN)
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                AsyncImage(
                    model = currentUser?.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.default_avatar),
                )
                Text(
                    text = currentUser?.nickName ?: "未命名",
                    modifier = Modifier
                        .padding(start = 140.dp, top = 10.dp),
                    fontSize = 20.sp
                )
                Text(
                    text = "手机号：" + currentUser?.phone,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(start = 140.dp, top = 70.dp),
                    color = WechatGray4
                )
            }
        }
        // 菜单项
        items.forEach { item ->
            MenuItem(
                icon = item.icon,
                title = item.title,
                currentValue = null,
                displayArrow = true,
                onClick = {
                    if (item.route != null) {
                        navController.navigate(item.route)
                    }
                })
        }
    }
}

sealed class MeMenu(val icon: Int, val title: String, val route: String?) {
    object Service : MeMenu(R.drawable.me_menu_service, "服务", null)
    object Like : MeMenu(R.drawable.me_menu_like, "收藏", null)
    object CardBag : MeMenu(R.drawable.me_menu_cardbag, "卡包", null)
    object Setting : MeMenu(R.drawable.me_menu_settings, "设置", MeRoutes.SETTING_SCREEN)
}