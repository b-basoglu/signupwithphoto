package com.bbasoglu.signup.ui.signup.adapter.data

import com.bbasoglu.uimodule.adapter.BaseAdapterData


data class SignUpHeaderData(
    override val id: String?,
    var title: String? = null,
    var subTitle: String? = null,
    var avatarText: String? = null,
    var path: String? = null,
) : BaseAdapterData