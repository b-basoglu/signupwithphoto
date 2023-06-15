package com.bbasoglu.signup.ui.login

import com.bbasoglu.core.data.SignUpCoreDataStore
import com.bbasoglu.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUpCoreDataStore: SignUpCoreDataStore,
): BaseViewModel(){

    suspend fun getUserName() = signUpCoreDataStore.getUserName()
    suspend fun getEmail() = signUpCoreDataStore.getEmail()
    suspend fun getImagePath() = signUpCoreDataStore.getImagePath()
    suspend fun getWebAddress() = signUpCoreDataStore.getWebAddress()
}