package com.example.youxin.ui.component

import android.R.attr.top
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.youxin.R
import com.example.youxin.ui.theme.WechatGray4
import com.example.youxin.ui.theme.WechatGreen
import com.example.youxin.ui.theme.White

/**
 * 自定义菜单项
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItem(
    icon: Int? = null,// icon不为空时展示的是菜单项
    image: Int? = null,// image不为空时展示的是通讯录中的名片
    imageUri: String? = null,// imageUri不为空时展示的是通讯录中的名片
    title: String,
    currentValue: String? = null,// 右侧显示的当前值
    displayArrow: Boolean? = false,// 是否展示右侧的箭头
    displayTick: Boolean? = false, // 是否展示右侧绿色对勾
    onClick: () -> Unit
) {
    val startPadding =
        if (icon != null) 50.dp else if (image != null || imageUri != null) 65.dp else 20.dp
    Box(
        modifier = Modifier
            .padding(top = if (image != null || imageUri != null) (0.5).dp else 10.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(White)
            .clickable(
                onClick = {
                    onClick()
                }
            )
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 20.dp, top = 17.dp)
                    .size(17.dp),
                tint = Color.Unspecified
            )
        }
        if (image != null) {
            Icon(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 15.dp, top = 5.dp)
                    .size(40.dp),
                tint = Color.Unspecified
            )
        }
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 15.dp, top = 5.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.default_avatar),
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = startPadding)
        )
        // 右侧显示当前值
        Text(
            text = currentValue ?: "",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 30.dp),
            color = WechatGray4
        )
        // 最右侧箭头
        if (displayArrow != null && displayArrow) {
            Icon(
                painter = painterResource(R.drawable.right_arrow),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .size(20.dp),
                tint = WechatGray4
            )
        }
        // 最右侧对勾
        if (displayTick != null && displayTick) {
            Icon(
                painter = painterResource(R.drawable.tick),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .size(20.dp),
                tint = WechatGreen
            )
        }
    }
}
