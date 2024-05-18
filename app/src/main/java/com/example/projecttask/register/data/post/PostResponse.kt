package com.example.projecttask.register.data.post

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("post")
    val post: PostResult? = null
)
