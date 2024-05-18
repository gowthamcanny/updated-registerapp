package com.example.projecttask.signin.data

import com.google.gson.annotations.SerializedName

data class UserSignUpPostResult(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("name")
    var name: String? = null
)
