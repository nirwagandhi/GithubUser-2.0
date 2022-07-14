package com.gandhi.githubuser2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gandhi.githubuser2.retrofit.ItemsItem
import com.gandhi.githubuser2.R
import com.gandhi.githubuser2.data.database.UserEntity
import com.gandhi.githubuser2.databinding.ActivityFavoriteBinding
import com.gandhi.githubuser2.fragment.FollowerFragment
import com.gandhi.githubuser2.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var favoriteViewModel: FavoriteViewModel

    companion object {
        const val TAG = "Favorite_ACTIVITY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_GitHubUser2)
        setContentView(binding.root)

        supportActionBar?.title = "GitHub User Application"

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteList.layoutManager = layoutManager
        binding.rvFavoriteList.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteList.addItemDecoration(itemDecoration)

        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        showLoadings(true)



        favoriteViewModel.getAllFavorite().observe(this) { user ->
            showLoadings(false)
            if (user != null) {

                val mapped = map(user)
                setDataUser(mapped)
                Log.d(TAG, "onCreate: $user")

            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }


    private fun setDataUser(user: MutableList<ItemsItem>) {
        val adapter = UserAdapter(user)
        binding.rvFavoriteList.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.setOnClickCallBack(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem?) {
                val intent = Intent(this@FavoriteActivity, DetailUser::class.java)
                intent.putExtra(DetailUser.EXTRA_USER, data)
                intent.putExtra(FollowerFragment.EXTRA_USER, data)
                intent.putExtra(DetailUser.EXTRA_ID, data?.id)
                intent.putExtra(DetailUser.EXTRA_AVATAR, data?.avatarUrl)
                startActivity(intent)
                Log.d(TAG, "onItemClicked: $data ")
            }


        })
    }


    private fun showLoadings(isLoading: Boolean) {
        if (isLoading) {
            binding.rvFavoriteList.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

        } else {

            binding.progressBar.visibility = View.GONE
            binding.rvFavoriteList.visibility = View.VISIBLE

        }
    }

    private fun map(userEntity: List<UserEntity>): MutableList<ItemsItem> {
        val list: MutableList<ItemsItem> = ArrayList()
        for (user in userEntity) {
            val map = ItemsItem(
                user.login,
                user.avatar_url,
                user.id
            )

            list.add(map)
            binding.notFound.visibility = View.GONE
            binding.tvNotFound.visibility = View.GONE
        }
        return list
    }

}