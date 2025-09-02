package com.example.youxin.ui.component

import android.R.attr.onClick
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.youxin.R
import com.example.youxin.ui.theme.WechatGray1

/**
 * 自定义导航栏
 * @param title 标题 onClick 返回键点击事件 actions 右侧按钮
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: String,
    onClick: () -> Unit = {},
    colors: TopAppBarColors? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box {
        TopAppBar(
            title = {
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onClick()
                    },
                    modifier = Modifier
                        .size(25.dp)
                        .indication(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        modifier = Modifier.size(15.dp),
                        contentDescription = "返回",
                    )
                }
            },
            colors = colors
                ?: TopAppBarDefaults.topAppBarColors(
                    containerColor = WechatGray1
                ),
            actions = actions,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        )
        // 标题
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
        )
    }
}