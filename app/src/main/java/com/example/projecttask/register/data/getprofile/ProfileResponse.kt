package com.example.projecttask.register.data.getprofile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("post")
    val profilePosts: ProfileResult? = null
)