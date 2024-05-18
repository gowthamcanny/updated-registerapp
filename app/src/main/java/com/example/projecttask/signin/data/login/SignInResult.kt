package com.example.projecttask.signin.data.login

import com.google.gson.annotations.SerializedName

data class SignInResult(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null,
)
