package com.bbasoglu.core.extensions

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.bbasoglu.uimodule.dialog.popup.Popup

fun Context.showPopup(popup: Popup, onAction: ()->Unit){
    val popUpTitle = popup.title ?: ""
    val popUpMessage = popup.message ?: ""
    val positiveButtonText = popup.positiveButton?.text ?: getString(com.bbasoglu.uimodule.R.string.popup_ok)

    MaterialDialog(this).show {
        title(text = popUpTitle)
        message(text = popUpMessage)
        cancelable(false)
        cancelOnTouchOutside(false)
        positiveButton(text = positiveButtonText) { dialog ->
            onAction.invoke()
            dialog.dismiss()
        }
    }
}
