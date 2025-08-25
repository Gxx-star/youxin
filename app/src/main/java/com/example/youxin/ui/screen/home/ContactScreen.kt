package com.example.youxin.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.youxin.R
import com.example.youxin.ui.component.MenuItem
import com.example.youxin.ui.theme.WechatGray4
import com.example.youxin.ui.viewmodel.ContactViewModel
import com.example.youxin.utils.constant.NavConstants

/**
 * 通讯录页面
 */
@Composable
fun ContactScreen(
    navController: NavController,
    contactViewModel: ContactViewModel
) {
    val menuItems = listOf(
        ContactMenu.NewFriend,
        ContactMenu.GroupChar,
        ContactMenu.OfficialAccount
    )
    val scrollState = rememberLazyListState()
    val selectedLetter = remember { mutableStateOf<String?>(null) }
    val friendList by contactViewModel.currentFriendList.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState
        ) {
            // 菜单项
            items(menuItems.size) { index ->
                MenuItem(
                    image = menuItems[index].icon,
                    title = menuItems[index].title,
                    currentValue = null,
                    displayArrow = false,
                    onClick = {
                        if (menuItems[index].route != null) {
                            navController.navigate(menuItems[index].route.toString())
                        }
                    }
                )
            }
            // 好友列表
            friendList.forEach {(letter, friendList) ->
                if(friendList.isNotEmpty()){
                    item{
                        LetterSeparator(letter)
                    }
                    items(
                        friendList.size
                    ){
                        MenuItem(
                            imageUri = friendList[it].avatar,
                            title = friendList[it].nickName,
                            currentValue = null,
                            displayArrow = false,
                            onClick = {
                                // 好友详情页
                            }
                        )
                    }
                }
            }
        }
    }
}

// 字母导航组件
@Composable
fun LetterNavigation(
    letters: List<String>,
    selectedLetter: String?,
    onLetterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        letters.forEach { letter ->
            Text(
                text = letter,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onLetterClick(letter) }
                    .wrapContentSize(Alignment.Center),
                fontSize = 12.sp,
                color = if (letter == selectedLetter) Color(0xFF07C160) else Color(0xFF666666),
                fontWeight = if (letter == selectedLetter) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

sealed class ContactMenu(val icon: Int, val title: String, val route: String?) {
    object NewFriend : MeMenu(
        R.drawable.contact_menu_newfriend,
        "新的朋友",
        NavConstants.MainRoutes.ContactRoutes.NEW_FRIEND_SCREEN
    )

    object GroupChar : MeMenu(R.drawable.contact_menu_groupchat, "群聊", null)
    object OfficialAccount : MeMenu(R.drawable.contact_menu_officialaccount, "公众号", null)
}
// 字母分割符
@Composable
private fun LetterSeparator(letter: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color(0xFFE8E8E8)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = letter.uppercase(), // 字母大写显示
            style = MaterialTheme.typography.titleMedium,
            color = WechatGray4, // 字母文字色
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}