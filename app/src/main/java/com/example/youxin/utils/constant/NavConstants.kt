package com.example.youxin.utils.constant

/**
 * navigation配置
 */
object NavConstants {
    object RootRoutes {
        const val LOGIN_GRAPH = "login_graph"
        const val MAIN_GRAPH = "main_graph"
    }

    object LoginRoutes {
        const val LOGIN_SCREEN = "login_screen"
        const val QUICK_LOGIN_SCREEN = "quick_login_screen"
        const val FULL_LOGIN_SCREEN = "full_login_screen"
        const val REGISTER_SCREEN = "register_screen"
    }

    object MainRoutes {
        const val MAIN_CONTAINER = "main_container"
        const val YOUXIN_SCREEN = "youxin_screen"
        const val CONTACT_SCREEN = "contact_screen"
        const val DISCOVER_SCREEN = "discover_screen"
        const val ME_SCREEN = "me_screen"

        // 从[友信]打开的子页面
        object YouxinRoutes {
            const val CHAT_SCREEN = "chat_screen"
        }

        // 从[通讯录]打开的子页面
        object ContactRoutes {
            const val NEW_FRIEND_SCREEN = "new_friend_screen"
            const val ADD_FRIEND_SCREEN = "add_friend_screen"
            const val VERIFIED_FRIEND = "verified_friend"
            const val FRIEND_DETAIL_SCREEN = "friend_detail_screen"
            const val FRIEND_DATA_SETTINGS = "friend_data_settings"
            const val FRIEND_REMARK_SETTINGS = "friend_remark_settings"
            const val APPLY_FRIEND_SCREEN = "apply_friend_screen"
        }

        // 从[发现]打开的子页面
        object DiscoverRoutes {

        }

        // 从[我]打开的子页面
        object MeRoutes {
            const val MY_INFORMATION_SCREEN = "my_information_screen"
            const val SETTING_SCREEN = "setting_screen"
            const val UPDATE_NICKNAME = "update_nickname"
            const val UPDATE_SEX = "update_sex"
        }
    }
}