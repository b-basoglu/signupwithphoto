package com.bbasoglu.uimodule.dialog.popup

data class Popup(
    var message: String? = null,
    var title: String? = null,
    var positiveButton: PopupButton? = null,
)

data class PopupButton(
    var text: String? = null
)