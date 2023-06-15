package com.bbasoglu.uimodule.dialog.generic

import androidx.annotation.DrawableRes
import com.bbasoglu.uimodule.adapter.BaseAdapterData

data class SelectItemData(
    override val id: String?,
    @DrawableRes var leftDrawable:Int? = null
) : BaseAdapterData