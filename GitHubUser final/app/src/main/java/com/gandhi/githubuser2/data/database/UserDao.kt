package com.gandhi.githubuser2.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun insertToFavorite(userEntity: UserEntity)

    @Query("DELETE FROM fav_user WHERE id = :id")
    suspend fun delete(id: Int): Int

    @Query("SELECT * FROM fav_user ORDER BY id ASC")
     fun getAllFavorite(): LiveData<List<UserEntity>>

    @Query("SELECT COUNT(*) FROM fav_user WHERE id = :id ")
    suspend fun isFavorite(id: Int): Int


}