package com.bbasoglu.signup.ui.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bbasoglu.core.network.NetworkResponse
import com.bbasoglu.core.ui.BaseViewModel
import com.bbasoglu.signup.data.model.data.LoginFragmentDataModel
import com.bbasoglu.signup.data.model.ui.SignUpFragmentUiModel
import com.bbasoglu.signup.usecase.SignUpSaveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpSaveUseCase: SignUpSaveUseCase
): BaseViewModel(){

    private val mTriggerPhoto: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val triggerPhoto : StateFlow<Boolean?> = mTriggerPhoto

    private val mErrorStateFlow : MutableStateFlow<ErrorType?> = MutableStateFlow(null)
    val errorStateFlow : StateFlow<ErrorType?> = mErrorStateFlow

    private val mNavigatorStateFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val navigatorStateFlow : StateFlow<Boolean?> = mNavigatorStateFlow

    fun saveUserData(loginFragmentDataModel: SignUpFragmentUiModel){
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

    private fun saveUserDataUseCaseRunner(loginFragmentDataModel: SignUpFragmentUiModel){
        viewModelScope.launch {
            val result = signUpSaveUseCase.execute(
                SignUpSaveUseCase.Param(mapUiModel(loginFragmentDataModel))
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
    fun clearTriggerPhoto(){
        mTriggerPhoto.value = null
    }

    fun triggerPhotoStart(){
        mTriggerPhoto.value = true
    }

    private fun mapUiModel(loginFragmentDataModel: SignUpFragmentUiModel)=
        LoginFragmentDataModel(
            name = loginFragmentDataModel.name,
            website = loginFragmentDataModel.website,
            password = loginFragmentDataModel.password,
            path = loginFragmentDataModel.path,
            email = loginFragmentDataModel.email
        )

}