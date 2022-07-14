package com.gandhi.githubuser2.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseFollower(

    @field:SerializedName("ResponseFollower")
    val responseFollower: List<ResponseFollowerItem>
)

@Parcelize
data class ResponseFollowerItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("id")
    val id: Int,

    ) : Parcelable
