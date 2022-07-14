package com.gandhi.githubuser2.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.gandhi.githubuser2.retrofit.ItemsItem
import com.gandhi.githubuser2.R
import com.gandhi.githubuser2.databinding.ActivityDetailUserBinding
import com.gandhi.githubuser2.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUser : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailViewModel: DetailViewModel


    private var isFavorite = false

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_GitHubUser2)
        val view = binding.root
        setContentView(view)

        val data = intent.getParcelableExtra<ItemsItem>(EXTRA_USER)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)


        val bundle = Bundle()
        bundle.putString(EXTRA_USER, data?.login)

        val username = data?.login

        supportActionBar?.title = username

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]



        username!!.let { detailViewModel.setDetail(it) }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.detailUser.observe(this) { user ->
            setDataDetail(user)

        }

        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.isFavorite(id)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    binding.favoriteToggle.isChecked = true
                    isFavorite = true
                } else {
                    binding.favoriteToggle.isChecked = false
                    isFavorite = false
                }
            }
        }

        binding.favoriteToggle.setOnClickListener {
            isFavorite = !isFavorite

            if (isFavorite) {
                detailViewModel.insertToFavorite(id, username, avatar!!)
                Toast.makeText(this, "Added to Favorite List", Toast.LENGTH_SHORT).show()


            } else {
                detailViewModel.delete(id)
                Toast.makeText(this, "Deleted from the Favorite List", Toast.LENGTH_SHORT).show()
            }
            binding.favoriteToggle.isChecked = isFavorite
        }


        val sectionPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

    }


    private fun setDataDetail(user: ItemsItem) {
        Glide.with(this@DetailUser).load(user.avatarUrl).circleCrop().into(binding.imageView)
        binding.tvFollower.text = user.followers.toString()
        binding.tvFollowing.text = user.following.toString()
        binding.tvRepo.text = user.publicRepos.toString()
        binding.tvNameDetailUser.text = user.name
        val location = user.location
        val company = user.company
        if (location != null) {
            binding.tvLocationDetailUser.text = location
        } else {
            binding.tvLocationDetailUser.visibility = View.GONE
        }

        if (company != null) {
            binding.tvCompany.text = company
        } else {
            binding.tvCompany.text = "-"
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}
