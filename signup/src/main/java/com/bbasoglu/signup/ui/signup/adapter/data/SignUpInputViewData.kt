package com.bbasoglu.signup.ui.signup.adapter.data

import com.bbasoglu.signup.ui.signup.adapter.InputTextType
import com.bbasoglu.uimodule.adapter.BaseAdapterData


data class SignUpInputViewData(
    override val id: String?,
    var hint: String? = null,
    var text: String? = null,
    var input: InputTextType? = null,
    var isRequired: Boolean= false,
) : BaseAdapterData