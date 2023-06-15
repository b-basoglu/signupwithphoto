package com.bbasoglu.uimodule.dialog.generic

import com.bbasoglu.uimodule.adapter.BaseAdapter

class BottomSheetGenericAdapter (
    var selected: ((Any) -> Unit)? = null,
) : BaseAdapter(
    adapterRowTypes = listOf(
        BottomSheetGenericRow()
    ),
    adapterClick = selected
)