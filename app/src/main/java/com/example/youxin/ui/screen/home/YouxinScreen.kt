package com.example.youxin.ui.screen.home

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youxin.R
import com.example.youxin.data.db.entity.ChatLogEntity
import com.example.youxin.data.db.entity.ContactEntity
import com.example.youxin.ui.component.MyTopBar
import com.example.youxin.ui.theme.WechatGray1
import com.example.youxin.ui.theme.WechatGray2
import com.example.youxin.ui.theme.WechatGray4
import com.example.youxin.ui.theme.White
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.ChatViewModel
import com.example.youxin.utils.constant.NavConstants
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@Composable
fun YouxinScreen(
    navController: NavController,
    chatViewModel: ChatViewModel,
    appViewModel: AppViewModel
) {
    val scope = rememberCoroutineScope()
    val conversations by chatViewModel.conversations.collectAsState(emptyList())
    val listState = rememberLazyListState()
    val currentUserId by appViewModel.userIdFlow.collectAsState(null)
    LaunchedEffect(Unit) {
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WechatGray2),
        state = listState,
    ) {
        items(conversations.size) {
            Box(
                modifier = Modifier
                    .padding(top = 0.5.dp)
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(WechatGray1)
                    .clickable(onClick = {
                        scope.launch {
                            chatViewModel.updateCurrentConversationId(
                                chatViewModel.getConversationIdByUsersId(
                                    currentUserId.toString(),
                                    conversations[it].targetId
                                ).toString()
                            )
                        }
                        // 进入聊天页面
                        val encodeGson = Uri.encode(
                            GsonBuilder().create().toJson(
                                ContactEntity(
                                    id = conversations[it].targetId,
                                    nickName = conversations[it].targetNickname,
                                    avatar = conversations[it].targetAvatar,
                                    sex = 0,
                                    status = com.example.youxin.data.db.entity.FriendStatusEntity(
                                        false,
                                        false,
                                        false,
                                        null
                                    )
                                )
                            )
                        )
                        navController.navigate(
                            "${NavConstants.MainRoutes.YouxinRoutes.CHAT_SCREEN}/${encodeGson}"
                        )
                    })
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                ) {
                    AsyncImage(
                        model = conversations[it].targetAvatar,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = conversations[it].targetNickname,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = conversations[it].latestChatLog?.msgContent ?: "",
                            fontSize = 12.sp,
                            color = WechatGray4
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = conversations[it].latestChatLog?.sendTime?.toString() ?: "",
                        fontSize = 12.sp,
                        color = WechatGray4
                    )
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel,
    contactEntity: ContactEntity,
    appViewModel: AppViewModel
) {
    val inputMessage = remember {
        mutableStateOf("")
    }
    val currentUserId by appViewModel.userIdFlow.collectAsState(null)
    val chatLogs by chatViewModel.chatLogsFlow.collectAsState(emptyList())
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val currentConversationId by chatViewModel.currentConversationId.collectAsState(null)
    val isKeyboardOpen = WindowInsets.isImeVisible
    Log.d("myTag", "重组")
    LaunchedEffect(chatLogs.size,isKeyboardOpen) {
        if (chatLogs.isNotEmpty()) {
            listState.scrollToItem(chatLogs.lastIndex)
        }
    }
    Scaffold(
        topBar = {
            MyTopBar(
                title = contactEntity.nickName,
                onClick = {
                    navController.popBackStack()
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(WechatGray2)
        ) {
            // 聊天内容
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = false,
                state = listState,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
            ) {
                items(chatLogs.size) {
                    MessageItem(
                        message = chatLogs[it],
                        currentUserId = currentUserId.toString(),
                        currentUserAvatar = appViewModel.currentUser.value?.avatar,
                        targetUserAvatar = contactEntity.avatar.toString()
                    )
                }

            }
            //聊天框
            Row(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .background(WechatGray2)
                    .imePadding(),
                verticalAlignment = Alignment.Bottom // 图标和输入框底部对齐
            ) {
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.voice),
                        contentDescription = null
                    )
                }
                TextField(
                    value = inputMessage.value,
                    onValueChange = {
                        inputMessage.value = it.toString()
                    },
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .weight(1f)
                        .heightIn(
                            min = 36.dp,
                            max = 24.dp * 5
                        ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    ),
                    maxLines = 5,
                    minLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            scope.launch {
                                Log.d("myTag", currentConversationId.toString())
                                chatViewModel.sendMsg(
                                    currentConversationId.toString(),
                                    contactEntity.id,
                                    inputMessage.value
                                )
                                inputMessage.value = ""
                            }
                        }
                    )
                )
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.face),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

/**
 * 消息项
 */
@Composable
fun MessageItem(
    message: ChatLogEntity,
    currentUserId: String,
    currentUserAvatar: String?, // 当前用户头像
    targetUserAvatar: String, // 聊天对象头像
    modifier: Modifier = Modifier
) {
    // 判断是否是当前用户发送的消息
    val isSentByMe = message.sendId == currentUserId

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
    ) {
        // 接收消息时，头像在左边
        if (!isSentByMe) {
            AsyncImage(
                model = targetUserAvatar,
                contentDescription = "对方头像",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .padding(horizontal = 8.dp),
                contentScale = ContentScale.Crop
            )
        }

        // 消息内容
        Box(
            modifier = Modifier
                .clip(
                    if (isSentByMe) {
                        RoundedCornerShape(10.dp, 10.dp, 0.dp, 10.dp)
                    } else {
                        RoundedCornerShape(10.dp, 10.dp, 10.dp, 0.dp)
                    }
                )
                .background(if (isSentByMe) Color(0xFF95EC69) else White)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .wrapContentWidth()
        ) {
            Text(
                text = message.msgContent,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(2.dp)
            )
        }

        // 发送消息时，头像在右边
        if (isSentByMe) {
            AsyncImage(
                model = currentUserAvatar ?: R.drawable.default_avatar, // 默认头像
                contentDescription = "我的头像",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .padding(horizontal = 8.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

