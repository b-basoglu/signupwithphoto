package com.bbasoglu.uimodule.extensions

import android.widget.TextView

fun TextView.setTextWithAnimation(text: String, duration: Long = 150, completion: (() -> Unit)? = null){
    fadeOutAnimation(duration){
        this.text = text
        fadeInAnimation(duration){
            completion?.let {
                it()
            }
        }
    }
}