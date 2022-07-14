package com.gandhi.githubuser2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gandhi.githubuser2.retrofit.ItemsItem
import com.gandhi.githubuser2.R


class UserAdapter(private val userList : MutableList<ItemsItem>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userList = userList[position]
        Glide.with(holder.itemView).load(userList.avatarUrl).circleCrop().into(holder.imagePhoto)
        holder.name.text = userList.login
        holder.itemView.setOnClickListener {onItemClickCallback.onItemClicked(userList)}

    }

    override fun getItemCount(): Int = userList.size


    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagePhoto: ImageView = view.findViewById(R.id.img_item_user)
        val name: TextView = view.findViewById(R.id.tv_item_name_list)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem?)
    }

    fun setOnClickCallBack(onItemClickCallBack: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallBack
    }

}

