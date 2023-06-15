package com.bbasoglu.uimodule.adapter


import android.view.ViewGroup
import kotlin.reflect.KClass

abstract class BaseAdapterRow<VH : BaseViewHolder<*, *>, D : BaseAdapterData> {
    abstract fun getViewType(): Int
    abstract fun getDataType(): KClass<out BaseAdapterData>
    abstract fun onCreateViewHolder(
        parent: ViewGroup,
        adapter: BaseAdapter,
        adapterClick: ((Any) -> Unit)? = null
    ): VH

    abstract fun onBindViewHolder(holder: VH, data: D, position: Int)
}