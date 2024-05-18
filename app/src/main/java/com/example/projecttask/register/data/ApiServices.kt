package com.example.projecttask.register.data

import com.example.projecttask.register.data.delete.DeleteResponse
import com.example.projecttask.register.data.getprofile.GetAllProfileResponse
import com.example.projecttask.register.data.getprofile.ProfileResponse
import com.example.projecttask.register.data.post.PostResponse
import com.example.projecttask.register.data.post.PostResult
import com.example.projecttask.signin.data.SignUpPostResponse
import com.example.projecttask.signin.data.UserSignUpPostResult
import com.example.projecttask.signin.data.login.SignInResult
import com.example.projecttask.signin.data.login.SignInResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServices {
    @GET("feed/post/{postId}")
    suspend fun getProfile(
        @Path("postId") postId: String,
        @Header("Content-Type") contentType: String,
        @Header("Authorization") auth:String
    ): ProfileResponse

    @POST("feed/post")
    suspend fun postProfile(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") auth:String,
        @Body postProfileResult: PostResult
    ): PostResponse

    @GET("feed/posts")
    suspend fun getAllProfile(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") auth:String
    ): GetAllProfileResponse

    @PUT("feed/post/{postId}")
    suspend fun getUpdateProfile(
        @Path("postId") postId: String,
        @Header("Authorization") auth:String,
        @Body postProfileResult: PostResult
    ): PostResponse

    @DELETE("feed/post/{postId}")
    suspend fun getDeleteProfile(
        @Path("postId") postId: String,
        @Header("Authorization") auth:String
    ): DeleteResponse

    @PUT("auth/signup")
    suspend fun getSignIn(
        @Header("Content-Type") contentType: String,
        @Body userPostResult: UserSignUpPostResult
    ): SignUpPostResponse

    @POST("auth/login")
    suspend fun getLogIn(
        @Header("Content-Type") contentType: String,
        @Body userPostResult: SignInResult
    ): SignInResponse
}