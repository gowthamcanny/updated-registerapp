package com.example.projecttask.register.data.getprofile

import com.google.gson.annotations.SerializedName

data class GetAllProfileResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("posts")
    val profilePosts: List<ProfileResult>? = null
)
