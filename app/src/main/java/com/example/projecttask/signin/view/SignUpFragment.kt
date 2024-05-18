package com.example.projecttask.signin.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.projecttask.R
import com.example.projecttask.databinding.FragmentSignUpBinding
import com.example.projecttask.networking.Resource
import com.example.projecttask.register.repository.RegisterRepository
import com.example.projecttask.register.viewmodel.RegisterViewModel
import com.example.projecttask.signin.data.SignUpPostResponse
import com.example.projecttask.signin.data.UserSignUpPostResult
import com.example.projecttask.utils.NewDialogFragment
import com.example.projecttask.utils.ViewModelFactory

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        observeData()
        setListener()
        clickListener()
    }

    private fun setViewModel() {
        registerViewModel = ViewModelProvider(this, ViewModelFactory(RegisterRepository())).get(
            RegisterViewModel::class.java
        )
    }

    private fun observeData() {
        registerViewModel.getSignInPostLiveData().observe(viewLifecycleOwner, signInPost)
    }

    private var signInPost = Observer<Resource<SignUpPostResponse>> { response ->
        when (response.status) {
            Resource.Status.LOADING -> {
                Log.d("LoadingSignInPost", "SignIn loading")
            }

            Resource.Status.SUCCESS -> {
                Log.d("LoadingSignInPostSuccess", "SignIn Success")
                response.data?.message?.let { successPopup(it) }
            }

            Resource.Status.ERROR -> {
                Log.d("LoadingSignInPostOne", "SignIn Error")
                response.data?.message?.let { errorPopup(it) }
            }
        }
    }

    private fun successPopup(message: String) {
        binding.imgBlurEffect.isVisible = true
        val dialog = NewDialogFragment(requireContext())
        dialog.setCanceledOnTouchOutside(false)
        dialog.setMessage(message)
        dialog.setEmoji(R.drawable.positive_emoji)
        dialog.setPositiveButton("OK",
            DialogInterface.OnClickListener { dialogObj, which ->
                dialogObj?.apply {
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            binding.imgBlurEffect.isVisible = false
                            dialog.dismiss()
                            view?.findNavController()?.popBackStack()
                        }
                    }
                }
            })
        dialog.show()
    }

    private fun errorPopup(message: String) {
        binding.imgBlurEffect.isVisible = true
        val dialog = NewDialogFragment(requireContext())
        dialog.setCanceledOnTouchOutside(false)
        dialog.setMessage(message)
        dialog.setEmoji(R.drawable.negative_emoji)
        dialog.setPositiveButton("OK",
            DialogInterface.OnClickListener { dialogObj, which ->
                dialogObj?.apply {
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            binding.imgBlurEffect.isVisible = false
                            dialog.dismiss()
                        }
                    }
                }
            })
        dialog.show()
    }

    private fun setListener() {
        binding.passET.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                return@OnEditorActionListener true
            }
            false
        })

        binding.emailEt.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString()).matches()) {
                    binding.emailEt.error = getString(R.string.email_error)
                }
                return@OnEditorActionListener true
            }
            false
        })

        binding.nameEt.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                return@OnEditorActionListener true
            }
            false
        })

        binding.passET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableOrDisableSaveButton()
            }
        })

        binding.emailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableOrDisableSaveButton()
            }
        })

        binding.nameEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableOrDisableSaveButton()
            }
        })
    }

    private fun moveBack() {
        this.view?.findNavController()?.navigate(R.id.action_signUpFragment_to_signInFragment)
    }

    private fun enableOrDisableSaveButton() {
        var isEmailValid = false
        val isEmailIdFilled = binding.emailEt.text.toString().isNotEmpty()
        if (isEmailIdFilled) {
            isEmailValid =
                (Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString()).matches())
        }
        if (binding.passET.text?.isNotEmpty() == true && isEmailValid) {
            enableSaveButton()
        } else {
            disableSaveButton()
        }
    }

    private fun enableSaveButton() {
        binding.btnSignUp.isEnabled = true
        context?.let { ContextCompat.getColor(it, R.color.yellow) }
            ?.let { binding.btnSignUp.setBackgroundColor(it) }
    }

    private fun disableSaveButton() {
        binding.btnSignUp.isEnabled = false
        binding.btnSignUp.setBackgroundResource(R.drawable.register_bg_disbale)
    }

    private fun checkMandatoryFields(): Boolean {
        val isPassFilled = binding.passET.text.toString().isNotEmpty()

        val isEmailIdFilled = binding.emailEt.text.toString().isNotEmpty()

        val isNameFilled = binding.nameEt.text.toString().isNotEmpty()
        val isEmailValid =
            (isEmailIdFilled && Patterns.EMAIL_ADDRESS.matcher(
                binding.emailEt.text.toString()
            ).matches())
        return (isPassFilled && isEmailIdFilled && isEmailValid && isNameFilled)
    }

    private fun createProfile() {
        if (checkMandatoryFields()) {
            val pass = binding.passET.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val name = binding.nameEt.text.toString().trim()
            val eventData = UserSignUpPostResult(
                email = email,
                password = pass,
                name = name
            )
            registerViewModel.getSignInPost(eventData)
        }
    }

    private fun clickListener() {
        binding.btnSignUp.setOnClickListener {
            createProfile()
        }

        binding.tvAlready.setOnClickListener {
            moveBack()
        }
    }
}