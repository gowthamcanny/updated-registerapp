package com.example.projecttask.register.data.post

import com.google.gson.annotations.SerializedName


data class PostResult(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("number")
    var number: String? = null,
    @SerializedName("dob")
    var dob:String? = null,
    @SerializedName("email")
    var email:String? = null
)
