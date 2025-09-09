package com.example.youxin.ui.navigation

import android.R.attr.fontWeight
import android.R.attr.text
import android.R.attr.type
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.youxin.R
import com.example.youxin.data.db.entity.ApplyEntity
import com.example.youxin.data.db.entity.ContactEntity
import com.example.youxin.ui.screen.home.AddFriendScreen
import com.example.youxin.ui.screen.home.ApplyFriendScreen
import com.example.youxin.ui.screen.home.ContactDetailScreen
import com.example.youxin.ui.screen.home.YouxinScreen
import com.example.youxin.ui.screen.home.ContactScreen
import com.example.youxin.ui.screen.home.DiscoverScreen
import com.example.youxin.ui.screen.home.FriendDataSettingsScreen
import com.example.youxin.ui.screen.home.FriendRemarkSettingsScreen
import com.example.youxin.ui.screen.home.MeScreen
import com.example.youxin.ui.screen.home.NewFriendScreen
import com.example.youxin.ui.screen.home.PersonalDataScreen
import com.example.youxin.ui.screen.home.SettingScreen
import com.example.youxin.ui.screen.home.UpdateNicknameScreen
import com.example.youxin.ui.screen.home.UpdateSexScreen
import com.example.youxin.ui.screen.home.VerifiedFriendScreen
import com.example.youxin.ui.theme.WechatGray1
import com.example.youxin.ui.theme.WechatGreen
import com.example.youxin.ui.viewmodel.AppViewModel
import com.example.youxin.ui.viewmodel.ContactViewModel
import com.example.youxin.ui.viewmodel.MainViewModel
import com.example.youxin.ui.viewmodel.MeViewModel
import com.example.youxin.ui.viewmodel.PersonalDataViewModel
import com.example.youxin.ui.viewmodel.RegisterViewModel
import com.example.youxin.utils.constant.NavConstants
import com.example.youxin.utils.constant.NavConstants.MainRoutes
import com.example.youxin.utils.constant.NavConstants.MainRoutes.ContactRoutes
import com.google.gson.Gson
import com.google.gson.GsonBuilder

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("RestrictedApi", "UnrememberedGetBackStackEntry")
@Composable
fun MainContainer(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val appViewModel: AppViewModel = hiltViewModel()
    val personalDataViewModel: PersonalDataViewModel = hiltViewModel()
    val contactViewModel: ContactViewModel = hiltViewModel()
    val mainNavController = rememberNavController()
    val meViewModel: MeViewModel = hiltViewModel()
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in items.map { it.route }
    val showTopBar = currentRoute in listOf(
        MainScreen.Chat,
        MainScreen.Contact,
        MainScreen.Discover,
    ).map { it.route }
    Scaffold(
        topBar = {
            if (showTopBar)
                TopBar(navController = mainNavController)
        },
        bottomBar = {
            if (showBottomBar)
                BottomBar(navController = mainNavController)
        },
    ) { padding ->
        val contentPadding = if (showTopBar) padding else PaddingValues(0.dp)
        NavHost(
            navController = mainNavController,
            startDestination = MainRoutes.YOUXIN_SCREEN,
            modifier = Modifier
                .padding(contentPadding)
        ) {
            composable(MainRoutes.YOUXIN_SCREEN) { YouxinScreen(mainNavController) }
            composable(MainRoutes.CONTACT_SCREEN) {
                ContactScreen(
                    mainNavController,
                    contactViewModel
                )
            }
            composable(MainRoutes.ContactRoutes.NEW_FRIEND_SCREEN) {
                NewFriendScreen(
                    mainNavController,
                    appViewModel,
                    contactViewModel
                )
            }
            composable(MainRoutes.ContactRoutes.ADD_FRIEND_SCREEN) {
                AddFriendScreen(
                    mainNavController,
                    contactViewModel
                )
            }
            composable(MainRoutes.ContactRoutes.VERIFIED_FRIEND + "/{encodedGson}") { backStackEntry ->
                val encodedGson = backStackEntry.arguments?.getString("encodedGson")
                val applyGson = Uri.decode(encodedGson)
                val applyEntity = applyGson?.let {
                    GsonBuilder().create().fromJson(it, ApplyEntity::class.java)
                }
                applyEntity?.let {
                    VerifiedFriendScreen(
                        mainNavController,
                        contactViewModel,
                        it
                    )
                }
            }
            composable(MainRoutes.ContactRoutes.FRIEND_DETAIL_SCREEN + "/{encodedGson}") { backStackEntry ->
                val encodedGson = backStackEntry.arguments?.getString("encodedGson")
                val contactGson = Uri.decode(encodedGson)
                val contactEntity = contactGson?.let {
                    GsonBuilder().create().fromJson(it, ContactEntity::class.java)
                }
                contactEntity?.let {
                    ContactDetailScreen(
                        mainNavController,
                        contactViewModel,
                        it
                    )
                }
            }
            composable(MainRoutes.ContactRoutes.FRIEND_DATA_SETTINGS + "/{encodedGson}") {
                val contactEntity = Uri.decode(it.arguments?.getString("encodedGson"))?.let {
                    GsonBuilder().create().fromJson(it, ContactEntity::class.java)
                }
                contactEntity?.let {
                    FriendDataSettingsScreen(
                        mainNavController,
                        contactViewModel,
                        contactEntity
                    )
                }
            }
            composable(ContactRoutes.FRIEND_REMARK_SETTINGS+"/{encodeGson}"){
                val contactId = Uri.decode(it.arguments?.getString("encodeGson"))?.let{
                    GsonBuilder().create().fromJson(it, String::class.java)
                }
                contactId?.let {
                    FriendRemarkSettingsScreen(
                        mainNavController,
                        contactViewModel,
                        contactId
                    )
                }
            }
            composable(ContactRoutes.APPLY_FRIEND_SCREEN+"/{encodeGson}"){
                val contactEntity = Uri.decode(it.arguments?.getString("encodeGson"))?.let{
                    GsonBuilder().create().fromJson(it, ContactEntity::class.java)
                }
                contactEntity?.let {
                    ApplyFriendScreen(
                        mainNavController,
                        contactViewModel,
                        contactEntity
                    )
                }
            }
            composable(MainRoutes.DISCOVER_SCREEN) { DiscoverScreen(mainNavController) }
            composable(MainRoutes.ME_SCREEN) { MeScreen(mainNavController, appViewModel) }
            composable(MainRoutes.MeRoutes.SETTING_SCREEN) {
                SettingScreen(
                    mainNavController,
                    navController,
                    appViewModel
                )
            }
            composable(MainRoutes.MeRoutes.MY_INFORMATION_SCREEN) {
                PersonalDataScreen(
                    mainNavController,
                    appViewModel,
                    registerViewModel,
                    meViewModel
                )
            }
            composable(MainRoutes.MeRoutes.UPDATE_NICKNAME) {
                UpdateNicknameScreen(
                    mainNavController,
                    meViewModel
                )
            }
            composable(MainRoutes.MeRoutes.UPDATE_SEX) {
                UpdateSexScreen(
                    mainNavController,
                    meViewModel
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    NavigationBar(
        containerColor = WechatGray1,
        tonalElevation = 2.dp,
        modifier = Modifier
            .height(70.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                label = {
                    Text(
                        text = item.label,
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .offset(0.dp, 5.dp),
                    )
                },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WechatGreen,
                    selectedTextColor = WechatGreen,
                    indicatorColor = Color.Transparent // 选中后的背景色跟随父容器
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    val currentRoute = navController.currentDestination?.route
    val appBarTitle = when (currentRoute) {
        MainRoutes.YOUXIN_SCREEN -> "友信"
        MainRoutes.CONTACT_SCREEN -> "通讯录"
        MainRoutes.DISCOVER_SCREEN -> "发现"
        else -> ""
    }
    Box {
        TopAppBar(
            title = {

            },
            actions = {
                // 第一个图标：搜索
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.topbar_search),
                        contentDescription = "搜索",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                            .offset(10.dp, (-5).dp)
                    )
                }
                // 第二个图标：添加
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .indication(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() })
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.topbar_add),
                        contentDescription = "添加",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(17.dp)
                            .offset(0.dp, (-5).dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = WechatGray1
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        )
        Text(
            text = appBarTitle,
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(),
        )
    }
}

sealed class MainScreen(val route: String, val label: String, val icon: Int) {
    object Chat : MainScreen(MainRoutes.YOUXIN_SCREEN, "友信", R.drawable.home_chat)
    object Contact :
        MainScreen(MainRoutes.CONTACT_SCREEN, "通讯录", R.drawable.home_contact)

    object Discover :
        MainScreen(MainRoutes.DISCOVER_SCREEN, "发现", R.drawable.home_discover)

    object Me : MainScreen(MainRoutes.ME_SCREEN, "我", R.drawable.home_me)
}

val items = listOf(
    MainScreen.Chat,
    MainScreen.Contact,
    MainScreen.Discover,
    MainScreen.Me
)
