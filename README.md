# youxin
一款基于 Jetpack Compose 开发的仿微信客户端，旨在通过现代 Android 技术栈还原微信核心体验，同时探索 Compose 在复杂 UI 场景下的最佳实践。
技术栈​

    UI 框架：Jetpack Compose（声明式 UI，替代传统 XML）​
    架构：MVVM + 单 Activity 多 Compose 页面​
    状态管理：StateFlow + ViewModel​
    依赖注入：Hilt​
    网络：Retrofit + OkHttp（模拟微信 API 交互）​
    本地存储：Room（模拟聊天记录、联系人存储）​
    其他：Coil（图片加载）、Accompanist（Compose 扩展库）​

核心功能（规划中）​

    微信首页：聊天列表、联系人、发现、我的四大模块布局​
    聊天界面：文字消息、表情、图片发送​
    联系人页面：列表展示、搜索功能、联系人详情​
    基础交互：页面跳转动画、下拉刷新、长按删除会话​

项目目标​

    提供 Compose 开发复杂社交 App 的参考案例​
    实践 Compose 状态管理、UI 复用、动画等核心能力​
    打造贴近原生微信的流畅交互体验​

欢迎 Star 关注进度，也欢迎提交 PR 一起完善功能！
