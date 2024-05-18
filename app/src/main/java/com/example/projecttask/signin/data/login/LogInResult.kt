package com.example.projecttask.signin.data.login

import com.google.gson.annotations.SerializedName

data class LogInResult(
    @SerializedName("token")
    var token: String? = null,
    @SerializedName("userId")
    var userId: String? = null
)
