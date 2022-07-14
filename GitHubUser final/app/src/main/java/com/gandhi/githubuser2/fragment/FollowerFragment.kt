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
import com.gandhi.githubuser2.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment() {

    private var _binding : FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "FollowerFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followerViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FollowerViewModel::class.java]
        val username = requireActivity().intent.getParcelableExtra<ItemsItem>(EXTRA_USER) as ItemsItem

        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollower.layoutManager = layoutManager
        binding.rvFollower.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFollower.addItemDecoration(itemDecoration)


        followerViewModel.getFollowerList(username.login!!)
        Log.d(TAG, "onCreate: $username")

        followerViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        followerViewModel.userList.observe(viewLifecycleOwner) { user ->
            setFollower(user)
            Log.d(TAG, "onCreate: $user")

        }
    }

    private fun setFollower(user: List<ResponseFollowerItem> ) {
        val followerAdapter = FollowersAdapter(user)
        binding.rvFollower.adapter = followerAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}