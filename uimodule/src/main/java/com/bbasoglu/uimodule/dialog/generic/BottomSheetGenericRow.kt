package com.bbasoglu.uimodule.dialog.generic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bbasoglu.uimodule.R
import com.bbasoglu.uimodule.adapter.BaseAdapter
import com.bbasoglu.uimodule.adapter.BaseAdapterRow
import com.bbasoglu.uimodule.adapter.BaseViewHolder
import com.bbasoglu.uimodule.databinding.DialogBottomSheetRowBinding

class BottomSheetGenericRow :
    BaseAdapterRow<BottomSheetGenericRow.ViewHolder, SelectItemData>() {

    override fun getViewType() = R.layout.dialog_bottom_sheet_row
    override fun getDataType() = SelectItemData::class

    override fun onCreateViewHolder(
        parent: ViewGroup,
        adapter: BaseAdapter,
        adapterClick: ((Any) -> Unit)?
    ): ViewHolder {
        val binding = DialogBottomSheetRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, adapterClick)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        data: SelectItemData,
        position: Int
    ) {
        holder.bind(data, position)
    }

    inner class ViewHolder(
        binding: DialogBottomSheetRowBinding,
        val adapterClick: ((Any) -> Unit)?
    ) :
        BaseViewHolder<SelectItemData, DialogBottomSheetRowBinding>(
            binding
        ) {
        override fun bindHolder(data: SelectItemData, position: Int) {
            binding.itemText.text = data.id
            data.leftDrawable?.let {
                binding.itemText.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(binding.root.context,it),null,null,null)
                binding.itemText.compoundDrawablePadding = binding.root.context.resources.getDimensionPixelSize(R.dimen.text_size_space_drawable)
            }
            binding.root.setOnClickListener(View.OnClickListener {
                adapterClick?.invoke(data)
            })
        }
    }
}