package com.example.projecttask.register.view

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.projecttask.R
import com.example.projecttask.databinding.FragmentHomeBinding
import com.example.projecttask.networking.Resource
import com.example.projecttask.register.adapter.HomeProfileListAdapter
import com.example.projecttask.register.data.getprofile.GetAllProfileResponse
import com.example.projecttask.register.data.getprofile.ProfileResult
import com.example.projecttask.register.repository.RegisterRepository
import com.example.projecttask.register.viewmodel.RegisterViewModel
import com.example.projecttask.utils.Constants
import com.example.projecttask.utils.ViewModelFactory

class HomeFragment : Fragment(), HomeProfileListAdapter.OnProfileClick {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var registerViewModel: RegisterViewModel
    private var getAllProfileResponse: GetAllProfileResponse? = null
    private var authToken:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authToken = Constants.AUTH_TOKEN
        setViewModel()
        observeData()
        setListener()
        onBackPressed(view)
    }

    private fun onBackPressed(view: View) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                activity?.finish()
            }
            true
        }
    }

    private fun setViewModel() {
        registerViewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(RegisterRepository()))[RegisterViewModel::class.java]
    }

    private fun observeData() {
        registerViewModel.getAllProfile("Bearer $authToken")
        registerViewModel.getAllProfileLiveData().observe(viewLifecycleOwner, getAllUserLiveData)
    }

    private var getAllUserLiveData = Observer<Resource<GetAllProfileResponse?>> { response ->
        when (response.status) {
            Resource.Status.LOADING -> {
                Log.d("Loading", "loading")
            }
            Resource.Status.SUCCESS -> {
                Log.d("LoadingSuccess", "Success")
                getAllProfileResponse = response.data
                getAllProfileResponse?.profilePosts?.let {
                    if (it.isNotEmpty() && it != null) {
                        setRecycleView(it)
                        binding.rvProfile.isVisible = true
                    } else {
                        binding.tvProfile.isVisible = true
                    }
                }
            }
            Resource.Status.ERROR -> {
                Log.d("LoadingOne", "Error")
            }
        }
    }

    private fun setRecycleView(profilePosts: List<ProfileResult>) {
        binding.rvProfile.adapter = HomeProfileListAdapter(profilePosts, this)
    }

    private fun setListener() {
        binding.fbProfile.setOnClickListener {
            this.view?.findNavController()?.navigate(R.id.action_homeFragment_to_registerFragment)
        }
        binding.tvLogOut.setOnClickListener {
            this.view?.findNavController()?.navigate(R.id.action_homeFragment_to_signInFragment)
        }
    }

    override fun onProfileListener(profileResult: ProfileResult) {
        val bundle = bundleOf("post_id" to profileResult.id)
        this.view?.findNavController()?.navigate(R.id.action_homeFragment_to_profileFragment,bundle)
    }
}