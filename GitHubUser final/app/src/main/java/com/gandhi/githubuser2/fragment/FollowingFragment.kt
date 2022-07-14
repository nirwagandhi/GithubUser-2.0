package com.gandhi.githubuser2.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gandhi.githubuser2.retrofit.ItemsItem
import com.gandhi.githubuser2.retrofit.ResponseFollowerItem
import com.gandhi.githubuser2.databinding.FragmentFollowingBinding


class FollowingFragment : Fragment() {

    private var _binding : FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "followingFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followerViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FollowerViewModel::class.java]
        val username = requireActivity().intent.getParcelableExtra<ItemsItem>(EXTRA_USER)
        followerViewModel.getFollowingList(username?.login!!)

        followerViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followerViewModel.followingList.observe(viewLifecycleOwner) { user ->
            setFollowing(user)

            Log.d(TAG, "following: $user")
        }

    }

    private fun setFollowing(user: List<ResponseFollowerItem>) {
        val followerAdapter = FollowersAdapter(user)
        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.layoutManager = layoutManager
        binding.rvFollowing.setHasFixedSize(true)
        binding.rvFollowing.adapter = followerAdapter
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFollowing.addItemDecoration(itemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }



}