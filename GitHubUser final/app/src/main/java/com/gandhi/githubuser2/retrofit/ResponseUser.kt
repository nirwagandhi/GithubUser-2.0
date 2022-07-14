package com.gandhi.githubuser2.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseUser(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: MutableList<ItemsItem>? = null,
)
@Parcelize
data class ItemsItem(

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("public_repos")
	val publicRepos: Int? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("following")
	val following: Int? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("followers")
	val followers: Int? = null,

	@field:SerializedName("name")
	val name: String? = null

) : Parcelable


