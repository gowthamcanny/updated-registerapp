package com.example.projecttask.signin.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.projecttask.R
import com.example.projecttask.databinding.FragmentSignInBinding
import com.example.projecttask.networking.Resource
import com.example.projecttask.register.repository.RegisterRepository
import com.example.projecttask.register.viewmodel.RegisterViewModel
import com.example.projecttask.signin.data.login.SignInResponse
import com.example.projecttask.signin.data.login.SignInResult
import com.example.projecttask.utils.Constants
import com.example.projecttask.utils.NewDialogFragment
import com.example.projecttask.utils.ViewModelFactory

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed(view)
        setViewModel()
        observeData()
        setListener()
        clickListener()
    }

    private fun onBackPressed(view: View) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                activity?.finish()
            }
            true
        }
    }

    private fun setViewModel() {
        registerViewModel = ViewModelProvider(this, ViewModelFactory(RegisterRepository()))[RegisterViewModel::class.java]
    }

    private fun observeData() {
        registerViewModel.getLogInPostLiveData().observe(viewLifecycleOwner, logInPost)
    }

    private var logInPost = Observer<Resource<SignInResponse>> { response ->
        when (response.status) {
            Resource.Status.LOADING -> {
                Log.d("LoadingLogInPost", "LogIn loading")
            }

            Resource.Status.SUCCESS -> {
                Log.d("LoadingLogInPostSuccess", "LogIn Success")
                Constants.AUTH_TOKEN =  response.data?.token ?: ""
                val bundle = bundleOf("auth_token" to response.data?.token)
                this.view?.findNavController()?.navigate(R.id.action_signInFragment_to_homeFragment,bundle)
            }

            Resource.Status.ERROR -> {
                errorPopup(response.status.name)
                Log.d("LoadingLogInPostOne", "LogIn Error")
            }
        }
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
        binding.buttonSignIn.isEnabled = true
        context?.let { getColor(it, R.color.yellow) }
            ?.let { binding.buttonSignIn.setBackgroundColor(it) }
    }

    private fun disableSaveButton() {
        binding.buttonSignIn.isEnabled = false
        binding.buttonSignIn.setBackgroundResource(R.drawable.register_bg_disbale)
    }

    private fun checkMandatoryFields(): Boolean {
        val isPassFilled = binding.passET.text.toString().isNotEmpty()

        val isEmailIdFilled = binding.emailEt.text.toString().isNotEmpty()
        val isEmailValid =
            (isEmailIdFilled && Patterns.EMAIL_ADDRESS.matcher(
                binding.emailEt.text.toString()
            ).matches())
        return (isPassFilled && isEmailIdFilled && isEmailValid)
    }

    private fun createProfile() {
        if (checkMandatoryFields()) {
            val pass = binding.passET.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val eventData = SignInResult(
                email = email,
                password = pass
            )
            registerViewModel.getLogInPost(eventData)
        }
    }

    private fun clickListener() {
        binding.buttonSignIn.setOnClickListener {
            createProfile()
        }

        binding.tvRegistered.setOnClickListener {
            this.view?.findNavController()?.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }
}