package com.example.youxin.data.db.entity

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "applies")
data class ApplyEntity(
    @PrimaryKey()
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val gender: Int,
    @SerializedName("greet_msg")
    val greetMsg: String,
    val nickname: String,
    val status: Int
)