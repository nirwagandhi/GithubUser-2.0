package com.gandhi.githubuser2.retrofit

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getSearch(
        @Query("q") q: String
    ) : Call<ResponseUser>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<ItemsItem>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ResponseFollowerItem>>

    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ResponseFollowerItem>>

}