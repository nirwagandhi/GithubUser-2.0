package com.gandhi.githubuser2.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gandhi.githubuser2.retrofit.ItemsItem
import com.gandhi.githubuser2.R
import com.gandhi.githubuser2.databinding.ActivityMainBinding
import com.gandhi.githubuser2.databinding.ActivitySettingBinding
import com.gandhi.githubuser2.fragment.FollowerFragment
import com.gandhi.githubuser2.viewmodel.MainViewModel
import com.gandhi.githubuser2.viewmodel.SettingViewModel
import com.gandhi.githubuser2.viewmodel.ViewModelFactory
import com.gandhi.githubuser2.settings.SettingPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: ActivitySettingBinding
    private val mainViewModel by viewModels<MainViewModel>()


    companion object {
        const val TAG = "MainActivity"

    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GitHubUser2)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding2 = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchBar()
        settings()
        supportActionBar?.title = "GitHub User Application"




        mainViewModel.userList.observe(this) { user ->
            binding.notFound.visibility = View.GONE
            binding.tvNotFound.visibility = View.GONE
            setDataUser(user)
        }

        mainViewModel.isLoading.observe(this) {
            showLoadings(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserList.layoutManager = layoutManager
        binding.rvUserList.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserList.addItemDecoration(itemDecoration)

    }


    private fun searchBar() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.search_bar)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                query?.let { mainViewModel.getListUser(it) }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
    }


    private fun setDataUser(user: MutableList<ItemsItem>) {
        val userAdapter = UserAdapter(user)
        binding.rvUserList.adapter = userAdapter
        userAdapter.setOnClickCallBack(object : UserAdapter.OnItemClickCallback {

            override fun onItemClicked(data: ItemsItem?) {
                val intent = Intent(this@MainActivity, DetailUser::class.java)
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
            binding.rvUserList.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvUserList.visibility = View.VISIBLE

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.settings_menu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true

        }

    }

    private fun settings() {
        val switchTheme = binding2.switchTheme

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this , ViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) {
                isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

}

