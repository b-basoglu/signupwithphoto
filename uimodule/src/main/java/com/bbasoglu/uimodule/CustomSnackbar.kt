package com.bbasoglu.uimodule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bbasoglu.uimodule.databinding.LayoutSnackbarBinding
import com.bbasoglu.uimodule.extensions.invisible
import com.bbasoglu.uimodule.extensions.visible

class CustomSnackbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutSnackbarBinding.inflate(LayoutInflater.from(context), this)

    fun setText(text:String?){
        text?.let {
            binding.snackbarText.text = text
            visible()
            postDelayed(
                {
                invisible()
                },2000
            )
        }


    }

}
