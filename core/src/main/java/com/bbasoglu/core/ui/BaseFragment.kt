package com.bbasoglu.core.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bbasoglu.core.extensions.hideKeyBoard
import com.bbasoglu.uimodule.toolbar.StandardCustomToolbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment() {

    protected abstract val viewModel: BaseViewModel

    var activityListener : BaseActivity.ActivityListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityListener = context as BaseActivity.ActivityListener
        }catch (e : ClassCastException){
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        activityListener = null
        super.onDetach()
    }


    fun navigateTo(
        navDirections: NavDirections? = null,
        extras: ActivityNavigator.Extras? = null
    ){
        navDirections?.let {
            activityListener?.onNavigate(navDirections,extras)
        }
    }

    val onToolbarDefaultClick = object : StandardCustomToolbar.ToolbarIconClick {
        override fun onClick() {
            this@BaseFragment.hideKeyBoard()
            onBackPressed()
        }
    }

    fun onBackPressed() {
        findNavController().navigateUp()
    }


}