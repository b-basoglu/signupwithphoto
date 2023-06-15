package com.bbasoglu.signup.ui.signup

import androidx.lifecycle.viewModelScope
import com.bbasoglu.core.network.NetworkResponse
import com.bbasoglu.core.ui.BaseViewModel
import com.bbasoglu.signup.data.model.data.LoginFragmentDataModel
import com.bbasoglu.signup.usecase.SignUpSaveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpSaveUseCase: SignUpSaveUseCase
): BaseViewModel(){

    private val mErrorStateFlow : MutableStateFlow<ErrorType?> = MutableStateFlow(null)
    val errorStateFlow : StateFlow<ErrorType?> = mErrorStateFlow

    private val mNavigatorStateFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val navigatorStateFlow : StateFlow<Boolean?> = mNavigatorStateFlow

    fun saveUserData(loginFragmentDataModel: LoginFragmentDataModel){
        if (loginFragmentDataModel.email.isNullOrEmpty() && loginFragmentDataModel.password.isNullOrEmpty()){
            mErrorStateFlow.value = ErrorType.FILL_REMAINING
        }else if (loginFragmentDataModel.email.isNullOrEmpty()){
            mErrorStateFlow.value = ErrorType.FILL_EMAIL
        }else if (loginFragmentDataModel.password.isNullOrEmpty()){
            mErrorStateFlow.value = ErrorType.FILL_PASSWORD
        }else{
            saveUserDataUseCaseRunner(loginFragmentDataModel)
        }
    }

    fun saveUserDataUseCaseRunner(loginFragmentDataModel: LoginFragmentDataModel){
        viewModelScope.launch {
            val result = signUpSaveUseCase.execute(
                SignUpSaveUseCase.Param(loginFragmentDataModel)
            )
            when(result){
                is NetworkResponse.Error -> {
                    mErrorStateFlow.value = ErrorType.ERROR
                }
                is NetworkResponse.Loading -> {

                }
                is NetworkResponse.Success -> {
                    mNavigatorStateFlow.value = true
                }
            }
        }
    }

    fun clearErrorState(){
        mErrorStateFlow.value = null
    }

    fun clearNavigateState(){
        mNavigatorStateFlow.value = null
    }
}