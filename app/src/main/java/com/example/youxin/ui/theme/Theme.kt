package com.example.youxin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.W
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.youxin.ui.theme.Typography

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = WechatGreen,// 主色调
    onPrimary = Color.White,// 主色调上的文字颜色
    secondary = WechatDarkGreen,// 次色调
    onSecondary = Color.White,
    tertiary = WechatGreen,// 第三色调
    background = WechatLightGray,
)
// 全局形状
private val AppShapes = Shapes(
    // 按钮默认使用 medium 形状
    medium = RoundedCornerShape(8.dp), // 全局按钮8dp圆角
    small = RoundedCornerShape(4.dp), // 小按钮可使用4dp
)
// 全局排版（按钮文字样式）
private val AppTypography = Typography(
    // 按钮文字专用样式
    labelLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium, // 中等字重
        letterSpacing = 0.1.sp
    ),
    // 小按钮文字样式
    labelMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
    )
)

@Composable
fun YouxinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,  // 动态颜色，根据系统的主题色来设置
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}