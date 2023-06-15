package com.bbasoglu.signup.usecase

import com.bbasoglu.core.data.SignUpCoreDataStore
import com.bbasoglu.core.network.NetworkResponse
import com.bbasoglu.signup.data.model.data.LoginFragmentDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpSaveUseCase @Inject constructor(
    private val signUpCoreDataStore: SignUpCoreDataStore,
) {
    suspend fun execute(params: Param): NetworkResponse<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                params.body?.let { dataModel->
                    dataModel.path?.let { path->
                        signUpCoreDataStore.saveImagePath(path)
                    }
                    dataModel.website?.let { website->
                        signUpCoreDataStore.saveWebAddress(website)
                    }
                    dataModel.name?.let { name->
                        signUpCoreDataStore.saveNameUser(name)
                    }
                    dataModel.email?.let { email->
                        signUpCoreDataStore.saveEmail(email)
                    }
                    dataModel.password?.let { password->
                        signUpCoreDataStore.savePassword(password)
                    }
                    NetworkResponse.Success(Unit)
                }?:NetworkResponse.Error("not save")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse.Error("Exception caught")
        }
    }

    data class Param(val body: LoginFragmentDataModel?)
}