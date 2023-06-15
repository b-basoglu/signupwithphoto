package com.bbasoglu.core.extensions

import android.Manifest
import android.os.Build

val STORAGE_PERMISSION: String
    get() {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE;
        } else {
            Manifest.permission.READ_MEDIA_IMAGES;
        }
    }

