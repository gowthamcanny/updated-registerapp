package com.example.projecttask.profile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.text.DecimalFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.projecttask.R
import com.example.projecttask.networking.Resource
import com.example.projecttask.databinding.FragmentProfileBinding
import com.example.projecttask.register.data.delete.DeleteResponse
import com.example.projecttask.register.data.getprofile.ProfileResponse
import com.example.projecttask.register.data.getprofile.ProfileResult
import com.example.projecttask.register.data.post.PostResponse
import com.example.projecttask.register.data.post.PostResult
import com.example.projecttask.register.repository.RegisterRepository
import com.example.projecttask.register.viewmodel.RegisterViewModel
import com.example.projecttask.utils.Constants
import com.example.projecttask.utils.ViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var registerViewModel: RegisterViewModel
    private var profileResponse: ProfileResponse? = null
    private var postId: String? = null
    private var authToken:String? = null

    private var day = 0
    private var month: Int = 0
    private var year: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId = arguments?.getString("post_id")
        authToken = Constants.AUTH_TOKEN
        setListeners()
        setViewModel()
        observeData()
        clickListener()
    }

    private fun setViewModel() {
        registerViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(RegisterRepository()))[RegisterViewModel::class.java]
    }

    private fun observeData() {
        postId?.let { registerViewModel.getProfile(it,"Bearer $authToken") }
        registerViewModel.getProfileLiveData().observe(viewLifecycleOwner, getUserLiveData)
    }

    private var getUserLiveData = Observer<Resource<ProfileResponse?>> { response ->
        when (response.status) {
            Resource.Status.LOADING -> {
                Log.d("Loading", "loading")
            }

            Resource.Status.SUCCESS -> {
                Log.d("LoadingSuccess", "Success")
                profileResponse = response.data
                profileResponse?.profilePosts?.let { checkData(it) }
            }

            Resource.Status.ERROR -> {
                Log.d("LoadingOne", "Error")
            }
        }
    }

    private var updatePostProfile = Observer<Resource<PostResponse?>> { response ->
        when (response.status) {
            Resource.Status.LOADING -> {
                Log.d("LoadingUpdate", "Update loading")
            }
            Resource.Status.SUCCESS -> {
                Log.d("LoadingUpdateSuccess", "Update Success")
                toastMessage(response.data?.message ?: "")
                moveToRegistry()
            }
            Resource.Status.ERROR -> {
                Log.d("LoadingUpdateOne", "Update Error")
                toastMessage(response.data?.message ?: "")
            }
        }
    }

    private var deletePostProfile = Observer<Resource<DeleteResponse?>> { response ->
        when (response.status) {
            Resource.Status.LOADING -> {
                Log.d("LoadingDelete", "Delete loading")
            }
            Resource.Status.SUCCESS -> {
                Log.d("LoadingDeleteSuccess", "Delete Success")
                toastMessage(response.data?.message ?: "")
                moveToRegistry()
            }
            Resource.Status.ERROR -> {
                Log.d("LoadingDeleteOne", "Delete Error")
                toastMessage(response.data?.message ?: "")
            }
        }
    }

    private fun toastMessage(message:String) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }

    private fun moveToRegistry() {
        this.view?.findNavController()?.navigate(R.id.action_profileFragment_to_homeFragment)
    }

    private fun setListeners() {
        binding.etName.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (binding.etName.text?.isNotEmpty() == true) {
                    binding.etName.error = getString(R.string.name_error)
                }
                return@OnEditorActionListener true
            }
            false
        })

        binding.etNumber.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (binding.etNumber.text?.isEmpty() == true) {
                    binding.etNumber.error = getString(R.string.number_error)
                }
                return@OnEditorActionListener true
            }
            false
        })

        binding.etEmail.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                return@OnEditorActionListener true
            }
            false
        })

        binding.etNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableOrDisableSaveButton()
            }
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableOrDisableSaveButton()
            }
        })
    }

    private fun checkData(profileResult: ProfileResult) {
        binding.etName.text = Editable.Factory.getInstance().newEditable(profileResult.name)
        binding.etNumber.text = Editable.Factory.getInstance().newEditable(profileResult.number)
        binding.etDob.text = Editable.Factory.getInstance().newEditable(profileResult.dob)
        binding.etEmail.text = Editable.Factory.getInstance().newEditable(profileResult.email)
    }

    private fun enableSaveButton() {
        binding.btnUpdate.isEnabled = true
        binding.btnUpdate.setBackgroundResource(R.drawable.register_bg)
    }

    private fun disableSaveButton() {
        binding.btnUpdate.isEnabled = false
        binding.btnUpdate.setBackgroundResource(R.drawable.register_bg_disbale)
    }

    private fun enableOrDisableSaveButton() {
        var isEmailValid = false
        val isEmailIdFilled = binding.etEmail.text.toString().isNotEmpty()
        if (isEmailIdFilled) {
            isEmailValid =
                (Patterns.EMAIL_ADDRESS.matcher(
                    binding.etEmail.text.toString()
                ).matches())
        }
        if (binding.etName.text?.isNotEmpty() == true && (binding.etNumber.text?.isNotEmpty() == true && binding.etNumber.length() == 10) &&
            binding.etDob.text?.isNotEmpty() == true && isEmailValid && binding.etDob.text!!.isNotEmpty()
        ) {
            enableSaveButton()
        } else {
            disableSaveButton()
        }
    }

    private fun checkMandatoryFields(): Boolean {
        val isNameFilled = binding.etName.text.toString().isNotEmpty()

        val mobile = binding.etNumber.text.toString().isNotEmpty()

        val isDateEntered = binding.etDob.text.toString().isNotEmpty()

        val isEmailIdFilled = binding.etEmail.text.toString().isNotEmpty()
        val isEmailValid =
            (isEmailIdFilled && Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmail.text.toString()
            ).matches())
        return (isNameFilled && isEmailIdFilled && isDateEntered && isEmailValid && mobile)
    }

    private fun updateProfile() {
        if (checkMandatoryFields()) {
            val name = binding.etName.text.toString().trim()
            val phone = binding.etNumber.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val dob = binding.etDob.text.toString().trim()
            val eventData = PostResult(
                id = "",
                name = name,
                number = phone,
                dob = dob,
                email = email
            )
            registerViewModel.getUpdatePostProfile(postId ?: "", "Bearer $authToken",eventData)
            registerViewModel.getUpdatePostProfileLiveData().observe(viewLifecycleOwner, updatePostProfile)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun clickListener() {
        binding.etDob.setOnClickListener {
            val c = Calendar.getInstance()
            c.add(Calendar.YEAR, 18)
            val dpd = DatePickerDialog(
                requireContext(), R.style.calender_theme,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val mFormat = DecimalFormat("00")
                    binding.etDob.setText(
                        "" + mFormat.format(dayOfMonth) + "-" + mFormat.format(
                            monthOfYear
                        ) + "-" + mFormat.format(year)
                    )
                },
                year,
                month,
                day

            )
            dpd.datePicker.maxDate = c.timeInMillis
            dpd.show()
        }
        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }

        binding.imgBack.setOnClickListener {
            moveToRegistry()
        }

        binding.btnDelete.setOnClickListener {
            registerViewModel.getDeleteProfile(postId ?: "","Bearer $authToken")
            registerViewModel.getDeleteProfileLiveData().observe(viewLifecycleOwner,deletePostProfile)
        }
    }
}