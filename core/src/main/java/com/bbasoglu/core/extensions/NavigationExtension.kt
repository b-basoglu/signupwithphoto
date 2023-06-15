package com.bbasoglu.core.extensions

import android.net.Uri
import androidx.annotation.IdRes
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigate(direction: NavDirections? = null, extras: ActivityNavigator.Extras? = null, deeplink: Uri? = null) {
    direction?.let {
        currentDestination?.getAction(direction.actionId)?.run {
            extras?.let { extras ->
                navigate(it, extras)
            } ?: run {
                navigate(it)
            }
        } ?: kotlin.run {
        }
    } ?: run {
        deeplink?.let {
            navigate(it)
        }
    }
}

fun NavController.safeNavigate(
    @IdRes destinationId: Int,
    @IdRes id: Int
) {
    if(currentDestination?.id != destinationId) {
        navigate(id)
    }
}

fun NavController.safeNavigate(
    @IdRes destinationId: Int
) {
    if(currentDestination?.id != destinationId) {
        navigate(destinationId)
    }
}
