package com.bbasoglu.core.extensions

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Showing keyboard.
 */
fun Activity.showKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    try {
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    } catch (e: Exception) {
    }
}

/**
 * Showing keyboard of a view.
 *
 * @param view view to be focused.
 */
fun View.showKeyboard() {
    try {
        requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            this,
            InputMethodManager.SHOW_IMPLICIT
        )
    } catch (e: Exception) {
    }
}

/**
 * Showing keyboard of a view.
 *
 * @param view view to be focused.
 */
fun View.hideKeyboard() {
    try {
        (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager)!!
            .hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Exception) {
    }
}


/**
 * Hiding keyboard.
 */
fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    try {
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    } catch (e: Exception) {
    }
}

/**
 * Hiding keyboard of a view.
 *
 * @param view to be focused.
 */
fun Activity.hideKeyboard(view: View) {
    val inputMethodManager =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    try {
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {
    }
}

fun Fragment.showKeyboard() {
    val activity = this.requireActivity()
    activity.showKeyboard()
}

fun Fragment.hideKeyBoard() {
    val activity = this.requireActivity()
    activity.hideKeyboard()
}

fun Fragment.hideKeyBoard(view: View) {
    val activity = this.requireActivity()
    activity.hideKeyboard(view)
}

fun Fragment.showKeyboard(delay: Long) {
    Handler().postDelayed({
        val activity = this.requireActivity()
        activity.showKeyboard()
    }, delay)
}

fun Fragment.hideKeyBoard(delay: Long) {
    Handler().postDelayed({
        val activity = this.requireActivity()
        activity.hideKeyboard()
    }, delay)
}

fun Fragment.hideKeyBoard(view: View, delay: Long) {
    Handler().postDelayed({
        val activity = this.requireActivity()
        activity.hideKeyboard(view)
    }, delay)
}