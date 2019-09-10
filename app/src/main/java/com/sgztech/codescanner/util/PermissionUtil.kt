package com.sgztech.codescanner.util

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.sgztech.codescanner.R
import com.sgztech.codescanner.extension.showLog


object PermissionUtil {

    fun havePermissions(activity: AppCompatActivity, requestCode: Int): Boolean{
        if (Constants.CURRENT_VERSION_CODE >= Constants.VERSION_CODE_MARSHMALLOW) {
            return checkPermission(
                activity,
                Constants.CURRENT_VERSION_CODE,
                necessaryPermissions(),
                requestCode
            )
        } else {
            return true
        }
    }

    private fun necessaryPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.CAMERA
        )
    }

    private fun checkPermission(activity: AppCompatActivity, minVersionCode: Int, permissions: Array<String>, requestCode: Int): Boolean {

        var havePermission = false
        if (minVersionCode < Constants.VERSION_CODE_MARSHMALLOW || permissions.isEmpty()) {
            return false
        }

        if (Constants.CURRENT_VERSION_CODE >= minVersionCode) {
            try {
                if (activity.checkSelfPermission(permissions[0]) == Constants.PERMISSION_DENIED) {
                    activity.requestPermissions(permissions, requestCode)
                } else {
                    havePermission = true
                }
            } catch (e: Exception) {
                e.message?.let {
                    activity.showLog(it)
                }
            }
        } else {
            activity.showLog(activity.getString(R.string.msg_unnecessary_check_permission))
        }
        return havePermission
    }

    @JvmStatic
    fun checkResultPermission(activity: AppCompatActivity, grantResults: IntArray, permissions: Array<out String>): Boolean {
        if (grantResults.isNotEmpty() && grantResults[0] == Constants.PERMISSION_GRANTED) {
            activity.showLog(activity.getString(R.string.msg_permission_granted, permissions.asList()))
            return true
        } else {
            activity.showLog(activity.getString(R.string.msg_permission_not_granted_list, permissions.asList()))
            return false
        }
    }
}