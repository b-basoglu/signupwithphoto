package com.bbasoglu.uimodule.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bbasoglu.uimodule.R
import com.bbasoglu.uimodule.databinding.StandardToolbarBinding
import com.bbasoglu.uimodule.extensions.disableClickTemporarily
import com.bbasoglu.uimodule.extensions.dp2px
import com.bbasoglu.uimodule.extensions.setTextWithAnimation

class StandardCustomToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: StandardToolbarBinding =
        StandardToolbarBinding.inflate(LayoutInflater.from(context), this)


    fun setToolbar(toolbarData: StandardToolbarData?) {
        toolbarData?.let {
            setTitle(it.title,it.titleColorId,it.isTitleCenter,it.textAnimationEnabled,it.titleSize)
            setToolbarLeftIcon(it.leftIconResourceId,it.leftIconColorId,it.leftIconText, it.toolbarLeftIconClick)
            setToolbarRightIcon(it.rightIconResourceId,it.rightIconColorId, it.toolbarRightIconClick)
            visibility = ConstraintLayout.VISIBLE
        } ?: run { visibility = ConstraintLayout.GONE }
    }

    private fun setTitle(title: String?,titleColorId:Int?,isTitleCenter: Boolean,textAnimationEnabled: Boolean,titleSize :Float?) {
        binding.apply {
            if (isTitleCenter){
                toolbarTitleTV.gravity = Gravity.CENTER
            }else{
                toolbarTitleTV.gravity = Gravity.START
            }
            title?.let {
                if (toolbarTitleTV.text != title) {
                    if (textAnimationEnabled){
                        toolbarTitleTV.setTextWithAnimation(title)
                    }else{
                        toolbarTitleTV.text= title
                    }
                }
                toolbarTitleTV.visibility = ConstraintLayout.VISIBLE
            }
            titleColorId?.let {
                toolbarTitleTV.setTextColor(ContextCompat.getColor(context,titleColorId))
            }?:let {
                toolbarTitleTV.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
            }
            titleSize?.let {
                toolbarTitleTV.textSize = it.dp2px.toFloat()
            }
        }
    }


    private fun setToolbarLeftIcon(resourceId: Int?,colorId: Int?,leftIconText: String?, toolbarIconClick: ToolbarIconClick?) {
        binding.run {
            resourceId?.let {
                toolbarLeftIconIV.visibility = ConstraintLayout.VISIBLE
                if (resourceId!= DEFAULT_BUTTON){
                    toolbarLeftIconIV.setImageResource(resourceId)
                }else{
                    toolbarLeftIconIV.setImageResource(R.drawable.back_button)
                }

                toolbarLeftIconIV.setOnClickListener {
                    toolbarLeftIconIV.disableClickTemporarily()
                    toolbarIconClick?.onClick()
                }
            } ?: run { toolbarLeftIconIV.visibility = ConstraintLayout.GONE }

            colorId?.let {
                if (colorId!= DEFAULT_BUTTON){
                    toolbarLeftIconIV.setColorFilter(ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    toolbarLeftIconIV.setColorFilter(ContextCompat.getColor(context, R.color.colorOnPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }?:run {
                toolbarRightIconIV.setColorFilter(null);
            }
            leftIconText?.let {
                leftText.visibility = ConstraintLayout.VISIBLE
                leftText.text = it
                leftText.setOnClickListener {
                    leftText.disableClickTemporarily()
                    toolbarIconClick?.onClick()
                }
            } ?: run { leftText.visibility = ConstraintLayout.GONE }
        }
    }

    private fun setToolbarRightIcon(resourceId: Int?,colorId: Int?, toolbarIconClick: ToolbarIconClick?) {
        binding.run {
            resourceId?.let {
                toolbarRightIconIV.visibility = ConstraintLayout.VISIBLE
                if (resourceId!= DEFAULT_BUTTON){
                    toolbarRightIconIV.setImageResource(resourceId)
                }else{
                    toolbarRightIconIV.setImageResource(R.drawable.ic_settings)
                }
                toolbarRightIconIV.setOnClickListener {
                    toolbarRightIconIV.disableClickTemporarily()
                    toolbarIconClick?.onClick()
                }
            } ?: run { toolbarRightIconIV.visibility = ConstraintLayout.GONE }

            colorId?.let {
                if (colorId!= DEFAULT_BUTTON){
                    toolbarRightIconIV.setColorFilter(ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    toolbarRightIconIV.setColorFilter(ContextCompat.getColor(context, R.color.colorOnPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }?:run {
                toolbarRightIconIV.setColorFilter(null);
            }
        }
    }


    fun clearCallbacks(){
        binding.run {
            toolbarLeftIconIV.setOnClickListener(null)
            toolbarRightIconIV.setOnClickListener(null)
        }
    }

    companion object {
        const val DEFAULT_BUTTON = -1
    }

    interface ToolbarIconClick {
        fun onClick()
    }
}