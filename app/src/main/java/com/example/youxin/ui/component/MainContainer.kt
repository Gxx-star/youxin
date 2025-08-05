package com.example.youxin.ui.component

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.youxin.R
import com.example.youxin.ui.theme.WechatGreen
import com.example.youxin.ui.theme.WechatLightGray
import com.example.youxin.ui.theme.WechatLightGreen
import com.example.youxin.utils.constant.NavConstants

@Composable
fun MainContainer(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            Crossfade(
                targetState = currentRoute,
                animationSpec = tween(durationMillis = 200),
            ) { route ->
                content()
                Log.d("myTag", route.toString())
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    NavigationBar(
        containerColor = WechatLightGray,
        tonalElevation = 2.dp,
        modifier = Modifier
            .height(56.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                label = {
                    Text(
                        text = item.label,
                        //color = if(currentRoute == item.route) WechatGreen else Color.Black
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WechatGreen,
                    selectedTextColor = WechatGreen,
                    indicatorColor = WechatLightGray
                )
            )
        }
    }
}

sealed class MainScreen(val route: String, val label: String, val icon: Int) {
    object Chat : MainScreen(NavConstants.MainRoutes.YOUXIN_SCREEN, "友信", R.drawable.home_chat)
    object Contact :
        MainScreen(NavConstants.MainRoutes.CONTACT_SCREEN, "通讯录", R.drawable.home_contact)

    object Discover :
        MainScreen(NavConstants.MainRoutes.DISCOVER_SCREEN, "发现", R.drawable.home_discover)

    object Me : MainScreen(NavConstants.MainRoutes.ME_SCREEN, "我", R.drawable.home_me)
}

val items = listOf(
    MainScreen.Chat,
    MainScreen.Contact,
    MainScreen.Discover,
    MainScreen.Me
)