package com.example.projecttask.register.repository


import com.example.projecttask.networking.Resource
import com.example.projecttask.register.data.ApiServices
import com.example.projecttask.register.data.delete.DeleteResponse
import com.example.projecttask.register.data.getprofile.GetAllProfileResponse
import com.example.projecttask.register.data.getprofile.ProfileResponse
import com.example.projecttask.register.data.post.PostResponse
import com.example.projecttask.register.data.post.PostResult
import com.example.projecttask.signin.data.SignUpPostResponse
import com.example.projecttask.signin.data.UserSignUpPostResult
import com.example.projecttask.signin.data.login.SignInResponse
import com.example.projecttask.signin.data.login.SignInResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterRepository {

    private val apiService: ApiServices

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://register-app-202038a583d1.herokuapp.com/") //Replace with your laptop IP Address
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiServices::class.java)
    }

    //Single Post
    suspend fun fetchProfile(postId: String,auth:String): Resource<ProfileResponse> {
        return try {
            val response = apiService.getProfile(
                postId,
                "application/json",
                auth
            ) // Assuming getProfile() returns ProfileResponse
            Resource.success(response)
        } catch (e: Exception) {
            Resource.error(e, null)
        }
    }

    //Post profile
    suspend fun postProfile(auth:String,postProfileResult: PostResult): Resource<PostResponse> {
        return try {
            val response = apiService.postProfile(
                "application/json",
                auth,
                postProfileResult
            )
            Resource.success(response)
        } catch (e: Exception) {
            Resource.error(e, null)
        }
    }

    //List of Post
    suspend fun fetchAllProfile(auth:String): Resource<GetAllProfileResponse> {
        return try {
            val response =
                apiService.getAllProfile("application/json",auth) // Assuming getProfile() returns ProfileResponse
            Resource.success(response)
        } catch (e: Exception) {
            Resource.error(e, null)
        }
    }

    //Update post
    suspend fun updatePostProfile(postId: String,auth:String,postProfileResult: PostResult): Resource<PostResponse> {
        return try {
            val response = apiService.getUpdateProfile(
                postId,
                auth,
                postProfileResult
            )
            Resource.success(response)
        } catch (e: Exception) {
            Resource.error(e, null)
        }
    }

    //Delete post
    suspend fun deleteProfile(postId: String,auth:String): Resource<DeleteResponse> {
        return try {
            val response = apiService.getDeleteProfile(postId,auth)
            Resource.success(response)
        } catch (e: Exception) {
            Resource.error(e, null)
        }
    }

    //sign in
    suspend fun signInPost(userPostResult: UserSignUpPostResult): Resource<SignUpPostResponse> {
        return try {
            val response = apiService.getSignIn(
                "application/json",
                userPostResult
            )
            Resource.success(response)
        } catch (e: Exception) {
            Resource.error(e, null)
        }
    }

    //Log in
    suspend fun logInPost(logInResult: SignInResult): Resource<SignInResponse> {
        return try {
            val response = apiService.getLogIn(
                "application/json",
                logInResult
            )
            Resource.success(response)
        } catch (e: Exception) {
            Resource.error(e, null)
        }
    }
}