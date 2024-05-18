package com.example.projecttask.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.projecttask.R
import com.example.projecttask.databinding.FragmentDailogBinding

class NewDialogFragment(context: Context) : AlertDialog(context) {

    private lateinit var binding: FragmentDailogBinding
    private var message: String = ""
    private var emoji:Int? = null
    private var positiveButton: String = ""
    private var negativeButton: String = ""
    private var messageTextSize: Int = 0
    private var positiveButtonTextSize: Int = 0
    private var negativeButtonTextSize: Int = 0

    private var negativeClickListener: DialogInterface.OnClickListener? = null
    private var positiveClickListener: DialogInterface.OnClickListener? = null

    fun setMessage(message: String) {
        this.message = message
    }

    fun setEmoji(image:Int) {
        this.emoji = image
    }

    fun setMessageTextSize(id: Int) {
        this.messageTextSize = id
    }

    fun setPositiveButtonTextSize(id: Int) {
        this.positiveButtonTextSize = id
    }

    fun setNegativeButtonSize(id: Int) {
        this.negativeButtonTextSize = id
    }

    fun setNegativeButton(
        negativeButton: String,
        negativeClickListener: DialogInterface.OnClickListener?
    ) {
        this.negativeButton = negativeButton
        this.negativeClickListener = negativeClickListener
    }

    fun setPositiveButton(
        positiveButton: String,
        positiveClickListener: DialogInterface.OnClickListener?
    ) {
        this.positiveButton = positiveButton
        this.positiveClickListener = positiveClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_dailog, null, false)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.negativeButton = negativeButton
        binding.positiveButton = positiveButton
        binding.message = message
        Glide.with(binding.imgLogo)
            .load(emoji)
            .centerCrop()
            .into(binding.imgLogo)
        if (messageTextSize != 0) {
            binding.tvDescription.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(messageTextSize)
            )
        }
        if (positiveButtonTextSize != 0) {
            binding.tvPositive.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(positiveButtonTextSize)
            )
        }
        if (negativeButtonTextSize != 0) {
            binding.tvNegative.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(negativeButtonTextSize)
            )
        }
        binding.tvNegative.setOnClickListener {
            negativeClickListener?.onClick(this, DialogInterface.BUTTON_NEGATIVE)
        }

        binding.tvPositive.setOnClickListener {
            positiveClickListener?.onClick(this, DialogInterface.BUTTON_POSITIVE)
        }
        setCancelable(false)
    }
}