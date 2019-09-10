package com.sgztech.codescanner.util

import android.content.pm.PackageManager
import android.os.Build

class Constants {

    companion object{
        const val VERSION_CODE_MARSHMALLOW = Build.VERSION_CODES.M
        val CURRENT_VERSION_CODE = Build.VERSION.SDK_INT
        const val PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED
        const val PERMISSION_DENIED = PackageManager.PERMISSION_DENIED
    }
}