package com.example.projecttask.utils

import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment

interface FragmentTransactionListener {
    fun addReplaceFragment(
        fragment: Fragment,
        isReplaceFragment: Boolean,
        addToBackStack: Boolean,
        tag: String
    )

    fun openBottomSheetFragment(
        fragment: AppCompatDialogFragment
    )

    fun dismissBottomSheetFragment(
        fragment: AppCompatDialogFragment
    )


}