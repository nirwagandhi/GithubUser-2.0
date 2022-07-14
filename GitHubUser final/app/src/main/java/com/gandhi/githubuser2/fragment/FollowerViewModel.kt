package com.gandhi.githubuser2.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gandhi.githubuser2.retrofit.ResponseFollowerItem
import com.gandhi.githubuser2.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowerViewModel : ViewModel() {

    private val _followerList = MutableLiveData<List<ResponseFollowerItem>>()
    val userList : LiveData<List<ResponseFollowerItem>> = _followerList

    private val _followingList = MutableLiveData<List<ResponseFollowerItem>>()
    val followingList: LiveData<List<ResponseFollowerItem>> = _followingList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading


    companion object {
        const val TAG  = "FollowerViewHolder"
    }

    
    fun getFollowerList(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ResponseFollowerItem>> {
            override fun onResponse(
                call: Call<List<ResponseFollowerItem>>,
                response: Response<List<ResponseFollowerItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody!= null) {
                        _followerList.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseFollowerItem>>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message}")

            }


        })

    }

    fun getFollowingList(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ResponseFollowerItem>> {
            override fun onResponse(
                call: Call<List<ResponseFollowerItem>>,
                response: Response<List<ResponseFollowerItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followingList.postValue(response.body())
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseFollowerItem>>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message}")

            }


        })

    }
}