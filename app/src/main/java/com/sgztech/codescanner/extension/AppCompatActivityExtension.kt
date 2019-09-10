package com.sgztech.codescanner.extension

import android.util.Log
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.showLog(message: String) {
    Log.w("TAG_DEBUG", message)
}