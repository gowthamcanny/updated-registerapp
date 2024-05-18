package com.example.projecttask.register.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projecttask.R
import com.example.projecttask.databinding.ItemProfileListLayoutBinding
import com.example.projecttask.register.data.getprofile.ProfileResult

class HomeProfileListAdapter(private var profileResult: List<ProfileResult>, var onProfileClick:OnProfileClick) :
    RecyclerView.Adapter<HomeProfileListAdapter.ProfileViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileViewHolder {
        val profileList = DataBindingUtil.inflate<ItemProfileListLayoutBinding>(
            LayoutInflater.from(parent.context), R.layout.item_profile_list_layout, parent, false
        )
        return ProfileViewHolder(profileList)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val bind = profileResult[position]
        holder.bind(bind)
    }

    override fun getItemCount(): Int = profileResult.size

    inner class ProfileViewHolder(val binding: ItemProfileListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ProfileResult) {
            binding.tvName.text = profile.name
            binding.tvNumber.text = profile.number
            binding.tvEmail.text = profile.email
            binding.cvProfile.setOnClickListener {
                onProfileClick.onProfileListener(profile)
            }
        }
    }

    interface OnProfileClick {
        fun onProfileListener(profileResult: ProfileResult)
    }
}