package com.bbasoglu.signup.ui.signup.adapter

import com.bbasoglu.signup.data.model.data.LoginFragmentDataModel
import com.bbasoglu.signup.ui.signup.adapter.data.SignUpHeaderData
import com.bbasoglu.signup.ui.signup.adapter.data.SignUpInputViewData
import com.bbasoglu.signup.ui.signup.adapter.row.SignUpHeaderRow
import com.bbasoglu.signup.ui.signup.adapter.row.SignUpInputViewRow
import com.bbasoglu.uimodule.adapter.BaseAdapter

class SignUpAdapter(
    var itemSelected: ((Any) -> Unit)? = null,
) : BaseAdapter(
    adapterRowTypes = listOf(
        SignUpInputViewRow(),
        SignUpHeaderRow(),
    ),
    adapterClick = itemSelected
){

    fun insertImage(path: String){
        currentList.forEachIndexed { index, baseAdapterData ->
            if (baseAdapterData is SignUpHeaderData){
                baseAdapterData.path = path
                notifyItemChanged(index,baseAdapterData)
            }
        }
    }

    fun getLoginData():LoginFragmentDataModel{
        val result= LoginFragmentDataModel()
        currentList.forEachIndexed { index, baseAdapterData ->
            if (baseAdapterData is SignUpHeaderData){
                result.path = baseAdapterData.path
            }else if (baseAdapterData is SignUpInputViewData){
                when(baseAdapterData.input){
                    InputTextType.FIRST_NAME -> {
                        result.name = baseAdapterData.text
                    }
                    InputTextType.EMAIL -> {
                        result.email = baseAdapterData.text
                    }
                    InputTextType.PASSWORD -> {
                        result.password = baseAdapterData.text
                    }
                    InputTextType.WEBSITE -> {
                        result.website = baseAdapterData.text
                    }
                    else -> {

                    }
                }
            }
        }
        return result

    }
}