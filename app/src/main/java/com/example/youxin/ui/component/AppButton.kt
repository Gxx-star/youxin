package com.example.youxin.ui.component

import android.R.attr.enabled
import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp), // 固定高度（主题中不适合定义尺寸，在组件中统一）
        enabled = enabled && !isLoading,
        // 直接使用主题中的颜色、形状
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = MaterialTheme.shapes.medium, // 使用主题中的圆角
    ) {
        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = androidx.compose.ui.Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            androidx.compose.material3.Text(
                text = text,
                style = MaterialTheme.typography.labelLarge // 使用主题中的文字样式
            )
        }
    }
}