package com.gandhi.githubuser2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gandhi.githubuser2.data.database.UserDao
import com.gandhi.githubuser2.data.database.UserEntity
import com.gandhi.githubuser2.data.database.UserRoomDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val mUserDao: UserDao
    private val db: UserRoomDatabase = UserRoomDatabase.getDatabase(application)


    init {
        mUserDao = db.userDao()
    }

    fun getAllFavorite(): LiveData<List<UserEntity>> {
        return mUserDao.getAllFavorite()
    }
}