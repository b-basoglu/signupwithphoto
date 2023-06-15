package com.bbasoglu.signup.data.model.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpFragmentUiModel (
    var path:String? = null,
    var website:String? = null,
    var name:String? = null,
    var email:String? = null,
    var password:String? = null,
        ):Parcelable