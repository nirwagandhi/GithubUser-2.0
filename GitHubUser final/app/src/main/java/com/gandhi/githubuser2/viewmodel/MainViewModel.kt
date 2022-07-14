package com.gandhi.githubuser2.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.gandhi.githubuser2.retrofit.ItemsItem
import com.gandhi.githubuser2.retrofit.ResponseUser
import com.gandhi.githubuser2.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _userList = MutableLiveData<MutableList<ItemsItem>>()
    val userList: LiveData<MutableList<ItemsItem>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading



    companion object {
        private const val TAG = "MainViewModel"
    }


    fun getListUser(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearch(q)
        client.enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userList.value = response.body()?.items!!
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })

    }



}