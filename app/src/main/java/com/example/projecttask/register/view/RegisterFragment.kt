package com.example.projecttask.register.view

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
import com.example.projecttask.databinding.FragmentRegisterBinding
import com.example.projecttask.networking.Resource
import com.example.projecttask.register.data.post.PostResponse
import com.example.projecttask.register.data.post.PostResult
import com.example.projecttask.register.repository.RegisterRepository
import com.example.projecttask.register.viewmodel.RegisterViewModel
import com.example.projecttask.utils.Constants
import com.example.projecttask.utils.ViewModelFactory

class RegisterFragment:Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private var postResult: PostResult? = null
    private var authToken:String? = null

    private var day = 0
    private var month: Int = 0
    private var year: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authToken = Constants.AUTH_TOKEN
        setListeners()
        setViewModel()
        observeData()
        clickListener()
    }
    private fun setViewModel() {
        registerViewModel = ViewModelProvider(this, ViewModelFactory(RegisterRepository()))[RegisterViewModel::class.java]
    }

    private fun observeData() {
        registerViewModel.getPostProfileLiveData().observe(viewLifecycleOwner,postProfile)
    }

    private var postProfile = Observer<Resource<PostResponse?>> { response ->
        when(response.status) {
            Resource.Status.LOADING -> {
                Log.d("LoadingPost","post loading")
            }
            Resource.Status.SUCCESS -> {
                Log.d("LoadingPostSuccess","post Success")
                postResult = response.data?.post
                toastMessage(response.data?.message ?: "")
                moveBack()
            }
            Resource.Status.ERROR -> {
                Log.d("LoadingPostOne","post Error")
                toastMessage(response.data?.message ?: "")
            }
        }
    }

    private fun toastMessage(message:String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    private fun moveBack() {
        view?.findNavController()?.popBackStack()
    }

    private fun setListeners() {
        binding.etName.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                return@OnEditorActionListener true
            }
            false
        })

        binding.etNumber.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
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

    private fun enableSaveButton() {
        binding.btnRegister.isEnabled = true
        binding.btnRegister.setBackgroundResource(R.drawable.register_bg)
    }

    private fun disableSaveButton() {
        binding.btnRegister.isEnabled = false
        binding.btnRegister.setBackgroundResource(R.drawable.register_bg_disbale)
    }

    private fun enableOrDisableSaveButton() {
        var isEmailValid = false
        val isEmailIdFilled = binding.etEmail.text.toString().isNotEmpty()
        if(isEmailIdFilled) {
            isEmailValid = (Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches())
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

        val isEmailValid = (isEmailIdFilled && Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches())

        return (isNameFilled && isEmailIdFilled && isDateEntered && isEmailValid && mobile)
    }

    private fun createProfile() {
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
            authToken?.let { registerViewModel.getPostProfile("Bearer $it",eventData) }
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
                    binding.etDob.setText("" + mFormat.format(dayOfMonth) + "-" +mFormat.format( monthOfYear )+ "-" +mFormat.format( year))
                },
                year ,
                month,
                day

            )
            dpd.datePicker.maxDate = c.timeInMillis
            dpd.show()
        }
        binding.btnRegister.setOnClickListener {
            createProfile()
        }

        binding.imgBack.setOnClickListener {
            moveBack()
        }
    }
}