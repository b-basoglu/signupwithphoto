package com.bbasoglu.uimodule.inputview

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.bbasoglu.uimodule.databinding.LayoutInputViewBinding
import com.google.android.material.textfield.TextInputLayout

class InputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private var binding: LayoutInputViewBinding =
        LayoutInputViewBinding.inflate(LayoutInflater.from(context), this)

    fun setData(hintText: String?, isPassword: Boolean = false) {
        binding.run {
            inputTitle.run {
                hintText?.let { hint = it }
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                if (isPassword) transformationMethod = PasswordTransformationMethod()
            }
            hintText?.let { inputLayoutTitle.hint = it }
        }
    }

    fun setText(hintText: String?) {
        binding.run {
            inputTitle.run {
                hintText?.let {
                    text ->
                    setText(text)
                }
            }
        }
    }

    fun getInput() = binding.inputTitle.text.toString()

    fun resetInput() = binding.inputTitle.text?.clear()

    fun getInputView() = binding.inputTitle
}
