package com.bbasoglu.uimodule.toolbar


data class StandardToolbarData(
    var title: String? = null,
    var titleColorId: Int? = null,
    var isTitleCenter: Boolean = false,
    var leftIconResourceId: Int? = null,
    var leftIconColorId: Int? = null,
    var textAnimationEnabled: Boolean = true,
    var leftIconText: String? = null,
    var toolbarLeftIconClick: StandardCustomToolbar.ToolbarIconClick? = null,
    var rightIconResourceId: Int? = null,
    var rightIconColorId: Int? = null,
    var toolbarRightIconClick: StandardCustomToolbar.ToolbarIconClick? = null,
    var titleSize: Float? = null,
)