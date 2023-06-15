package com.bbasoglu.uimodule.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView

fun View.disableClickTemporarily(delay:Long=500){
    isClickable = false
    postDelayed({
        isClickable = true
    },delay)
}

fun View.fadeOutAnimation(
    duration: Long = 150,
    visibility: Int = INVISIBLE,
    completion: (() -> Unit)? = null
) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }
}

fun View.fadeInAnimation(
    duration: Long = 150,
    completion: (() -> Unit)? = null
) {
    alpha = 0f
    visibility = VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.setVisibility(isVisible: Boolean) {
    if (isVisible) this.visible() else this.gone()
}

fun ImageView.setImage(id: Int?) {
    id?.let {
        setImageResource(it)
        visible()
    } ?: run { gone() }
}

fun getPixels(dipValue: Int, context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dipValue.toFloat(),
        r.displayMetrics
    ).toInt()
}