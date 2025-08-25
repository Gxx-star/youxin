package com.example.youxin.network.info

import com.google.gson.annotations.SerializedName

/**
 * 群聊实体类
 */
class GroupInfo (
    @SerializedName("group_id")
    val groupId: String, // 群id
    @SerializedName("name")
    val name: String, // 群名称
    @SerializedName("owner_id")
    val ownerId: String, // 群主id
    @SerializedName("admin_ids")
    val adminIds:List<String>,// 管理员id列表
    @SerializedName("member_ids")
    val memberIds:List<String>, // 成员id列表
    @SerializedName("max_members")
    val maxMembers:Int // 最大成员数
)