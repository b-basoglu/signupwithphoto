package com.bbasoglu.signup.ui.signup.adapter.row

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bbasoglu.signup.R
import com.bbasoglu.signup.databinding.RowSignupInputBinding
import com.bbasoglu.signup.ui.signup.adapter.InputTextType
import com.bbasoglu.signup.ui.signup.adapter.data.SignUpInputViewData
import com.bbasoglu.uimodule.adapter.BaseAdapter
import com.bbasoglu.uimodule.adapter.BaseAdapterRow
import com.bbasoglu.uimodule.adapter.BaseViewHolder


class SignUpInputViewRow :
    BaseAdapterRow<SignUpInputViewRow.ViewHolder, SignUpInputViewData>() {

    override fun getViewType() = R.layout.row_signup_input

    override fun getDataType() = SignUpInputViewData::class

    override fun onCreateViewHolder(
        parent: ViewGroup,
        adapter: BaseAdapter,
        adapterClick: ((Any) -> Unit)?
    ): ViewHolder {
        val binding = RowSignupInputBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, adapterClick)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        data: SignUpInputViewData,
        position: Int
    ) {
        holder.bind(data, position)
    }

    inner class ViewHolder(
        binding: RowSignupInputBinding,
        val adapterClick: ((Any) -> Unit)?
    ) :
        BaseViewHolder<SignUpInputViewData, RowSignupInputBinding>(
            binding
        ) {
        override fun bindHolder(data: SignUpInputViewData, position: Int) {

            data.text?.let {
                binding.inputTitle.setText(it)
            }
            if (data.input == InputTextType.EMAIL){
                binding.inputTitle.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }else if (data.input == InputTextType.PASSWORD){
                binding.inputTitle.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.inputTitle.transformationMethod = PasswordTransformationMethod.getInstance();
            }else if (data.input == InputTextType.WEBSITE){
                binding.inputTitle.inputType = InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT
            }else{
                binding.inputTitle.inputType = InputType.TYPE_CLASS_TEXT
            }
            binding.inputTitle.hint = data.hint
            binding.inputLayoutTitle.hint = data.hint
            binding.inputTitle.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    data.text = s.toString()
                }
            })
        }
    }
}