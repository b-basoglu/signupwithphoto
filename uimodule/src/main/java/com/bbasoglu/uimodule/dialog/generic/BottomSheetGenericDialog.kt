package com.bbasoglu.uimodule.dialog.generic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbasoglu.uimodule.R
import com.bbasoglu.uimodule.adapter.BaseAdapterData
import com.bbasoglu.uimodule.databinding.DialogBottomSheetGenericDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetGenericDialog constructor(
    context: Context
) {
    private lateinit var mViewBinding: DialogBottomSheetGenericDialogBinding
    private var dialogView: BottomSheetDialog = BottomSheetDialog(context, R.style.GenericBottomSheetDialog)

    //onButtonClick returns position and item that is selected
    private var selectedListener: ((Int?, BaseAdapterData?) -> Unit)? = null
    private var selectorData: List<BaseAdapterData>? = null
    private var title: String? = null
    private var selectedIndexId: String? = null

    private var listAdapter = BottomSheetGenericAdapter(::onClick)

    companion object {
        const val NO_SELECTED_ITEM = -1
    }

    init {
        create()
    }

    fun setData(data: List<BaseAdapterData>, defaultId: String ?= null) {
        this.selectorData = data
        selectedIndexId = defaultId
    }

    fun setTitle(title: String = "") {
        this.title = title
    }

    fun setSelectedListener(selectedListener: (position: Int?, key: BaseAdapterData?)-> Unit){
        this.selectedListener = selectedListener
    }

    fun show(func: BottomSheetGenericDialog.() -> Unit): BottomSheetGenericDialog = apply {
        this.func()
        this.show()
    }

    private fun dismiss() {
        dialogView.dismiss()
    }

    private fun show() {
        initView()
        dialogView.show()
        dialogView.behavior.isFitToContents = true
    }

    private fun create(): BottomSheetDialog {
        return dialogView.apply {
            mViewBinding = DialogBottomSheetGenericDialogBinding.inflate(LayoutInflater.from(context))
            setContentView(mViewBinding.root)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
    private fun getItemFromId(itemId : String) : Pair<Int?,BaseAdapterData?>?{
        selectorData?.forEachIndexed { index, data->
            if(data.id == itemId){
                return Pair(index,data)
            }
        }
        return null
    }
    private fun initView() {
        title?.let {
            mViewBinding.textTitle.text = it
        }?:let {
            mViewBinding.textTitle.visibility = View.GONE
        }

        mViewBinding.listView.apply {
            selectorData?.let {
                adapter = listAdapter
                selectorData?.let {
                    listAdapter.submitList(it)
                }
                layoutManager = LinearLayoutManager(context)
                selectedIndexId?.let { id ->
                    scrollToPosition(selectorData?.indexOfFirst { it.id == id}?:0)
                }
            }
        }
    }

    private fun onClick(data: Any) {
        when (data) {
            is SelectItemData-> {
                selectedListener?.invoke(selectorData?.indexOfFirst { it.id == data.id},data)
                this.dismiss()
            }
        }
    }
}