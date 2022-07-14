package com.gandhi.githubuser2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gandhi.githubuser2.retrofit.ItemsItem
import com.gandhi.githubuser2.data.database.UserDao
import com.gandhi.githubuser2.data.database.UserEntity
import com.gandhi.githubuser2.data.database.UserRoomDatabase
import com.gandhi.githubuser2.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val mUserDao: UserDao
    private val db: UserRoomDatabase = UserRoomDatabase.getDatabase(application)

    private val _detailUser = MutableLiveData<ItemsItem>()
    val detailUser: LiveData<ItemsItem> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        const val TAG = "DetailActivity"
    }

    init {
        mUserDao = db.userDao()
    }


    fun setDetail(data: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(data)
        client.enqueue(object : Callback<ItemsItem> {
            override fun onResponse(call: Call<ItemsItem>, response: Response<ItemsItem>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.postValue(response.body())
                    }

                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ItemsItem>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message}")
            }


        })

    }

      fun insertToFavorite(id: Int, login: String, avatar_url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserEntity(id, login, avatar_url)
            mUserDao.insertToFavorite(user)
        }
    }

    suspend fun isFavorite(id: Int) = mUserDao.isFavorite(id)

    fun delete(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mUserDao.delete(id)
        }
    }


}