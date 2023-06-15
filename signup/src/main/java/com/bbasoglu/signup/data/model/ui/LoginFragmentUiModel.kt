package com.bbasoglu.signup.data.model.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginFragmentUiModel (
    val path:String?,
    val website:String?,
    val name:String?,
    val email:String?,
        ): Parcelable