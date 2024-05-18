package com.example.projecttask.register.data.getprofile

import com.google.gson.annotations.SerializedName

data class ProfileResult(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("number")
    var number: String? = null,
    @SerializedName("dob")
    val dob: String? = null,
    @SerializedName("email")
    var email: String? = null
)
