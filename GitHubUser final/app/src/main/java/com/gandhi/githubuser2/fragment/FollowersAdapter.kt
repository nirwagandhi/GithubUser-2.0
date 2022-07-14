package com.gandhi.githubuser2.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gandhi.githubuser2.R
import com.gandhi.githubuser2.retrofit.ResponseFollowerItem

class FollowersAdapter (private val listFollower: List<ResponseFollowerItem> ) : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FollowersViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
    )

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        val userList = listFollower[position]
        Glide.with(holder.itemView).load(userList.avatarUrl).circleCrop().into(holder.imagePhoto)
        holder.name.text = userList.login
    }

    override fun getItemCount(): Int = listFollower.size

    class FollowersViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val imagePhoto: ImageView = view.findViewById(R.id.img_item_user)
        val name: TextView = view.findViewById(R.id.tv_item_name_list)
    }

}