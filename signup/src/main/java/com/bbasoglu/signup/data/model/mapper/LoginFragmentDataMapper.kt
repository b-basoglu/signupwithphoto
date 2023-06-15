package com.bbasoglu.signup.data.model.mapper

import com.bbasoglu.signup.data.model.data.LoginFragmentDataModel
import com.bbasoglu.signup.data.model.ui.LoginFragmentUiModel

object LoginFragmentDataMapper {
    operator fun invoke(
        loginFragmentDataModel: LoginFragmentDataModel?
    ): LoginFragmentUiModel {
        return LoginFragmentUiModel(
            name = loginFragmentDataModel?.name,
            path = loginFragmentDataModel?.path,
            website = loginFragmentDataModel?.website,
            email = loginFragmentDataModel?.email
        )
    }
}