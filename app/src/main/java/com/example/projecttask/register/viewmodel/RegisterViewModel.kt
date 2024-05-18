package com.example.projecttask.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttask.networking.Resource
import com.example.projecttask.register.data.delete.DeleteResponse
import com.example.projecttask.register.data.getprofile.GetAllProfileResponse
import com.example.projecttask.register.data.getprofile.ProfileResponse
import com.example.projecttask.register.data.post.PostResponse
import com.example.projecttask.register.data.post.PostResult
import com.example.projecttask.register.repository.RegisterRepository
import com.example.projecttask.signin.data.SignUpPostResponse
import com.example.projecttask.signin.data.UserSignUpPostResult
import com.example.projecttask.signin.data.login.SignInResult
import com.example.projecttask.signin.data.login.SignInResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val profileLiveData: MutableLiveData<Resource<ProfileResponse>> =
        MutableLiveData()

    fun getProfileLiveData(): MutableLiveData<Resource<ProfileResponse>> {
        return profileLiveData
    }

    fun getProfile(postId: String,auth:String) {
        profileLiveData.value = Resource.loading(null)
        var response: Resource<ProfileResponse>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response = registerRepository.fetchProfile(postId,auth)
                profileLiveData.postValue(response)
            }
        }
    }

    private val postProfileLiveData: MutableLiveData<Resource<PostResponse>> =
        MutableLiveData()

    fun getPostProfileLiveData(): MutableLiveData<Resource<PostResponse>> {
        return postProfileLiveData
    }

    fun getPostProfile(auth:String,postProfileResult: PostResult) {
        postProfileLiveData.value = Resource.loading(null)
        var response: Resource<PostResponse>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response = registerRepository.postProfile(auth,postProfileResult)
                postProfileLiveData.postValue(response)
            }
        }
    }

    private val profileAllLiveData: MutableLiveData<Resource<GetAllProfileResponse>> =
        MutableLiveData()

    fun getAllProfileLiveData(): MutableLiveData<Resource<GetAllProfileResponse>> {
        return profileAllLiveData
    }

    fun getAllProfile(auth:String) {
        profileAllLiveData.value = Resource.loading(null)
        var response: Resource<GetAllProfileResponse>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response = registerRepository.fetchAllProfile(auth)
                profileAllLiveData.postValue(response)
            }
        }
    }

    private val updatePostProfileLiveData: MutableLiveData<Resource<PostResponse>> =
        MutableLiveData()

    fun getUpdatePostProfileLiveData(): MutableLiveData<Resource<PostResponse>> {
        return updatePostProfileLiveData
    }

    fun getUpdatePostProfile(postId: String, auth:String,postProfileResult: PostResult) {
        updatePostProfileLiveData.value = Resource.loading(null)
        var response: Resource<PostResponse>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response = registerRepository.updatePostProfile(
                    postId = postId,
                    auth,
                    postProfileResult = postProfileResult
                )
                updatePostProfileLiveData.postValue(response)
            }
        }
    }

    private val deleteProfileLiveData: MutableLiveData<Resource<DeleteResponse>> =
        MutableLiveData()

    fun getDeleteProfileLiveData(): MutableLiveData<Resource<DeleteResponse>> {
        return deleteProfileLiveData
    }

    fun getDeleteProfile(postId: String,auth:String) {
        deleteProfileLiveData.value = Resource.loading(null)
        var response: Resource<DeleteResponse>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response = registerRepository.deleteProfile(postId,auth)
                deleteProfileLiveData.postValue(response)
            }
        }
    }

    private val singInPostLiveData: MutableLiveData<Resource<SignUpPostResponse>> =
        MutableLiveData()

    fun getSignInPostLiveData(): MutableLiveData<Resource<SignUpPostResponse>> {
        return singInPostLiveData
    }

    fun getSignInPost(postProfileResult: UserSignUpPostResult) {
        singInPostLiveData.value = Resource.loading(null)
        var response: Resource<SignUpPostResponse>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response = registerRepository.signInPost(postProfileResult)
                singInPostLiveData.postValue(response)
            }
        }
    }

    private val logInPostLiveData: MutableLiveData<Resource<SignInResponse>> =
        MutableLiveData()

    fun getLogInPostLiveData(): MutableLiveData<Resource<SignInResponse>> {
        return logInPostLiveData
    }

    fun getLogInPost(logInResult: SignInResult) {
        logInPostLiveData.value = Resource.loading(null)
        var response: Resource<SignInResponse>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response = registerRepository.logInPost(logInResult)
                logInPostLiveData.postValue(response)
            }
        }
    }
}