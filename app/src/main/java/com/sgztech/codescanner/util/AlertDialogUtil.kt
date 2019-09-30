package com.sgztech.codescanner.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

object AlertDialogUtil {

    fun buildSimpleDialog(context: Context, resourceTitle: Int, resourceMessage: Int): AlertDialog.Builder{
        return buildSimpleDialog(context, resourceTitle)
            .setMessage(context.getString(resourceMessage))
    }

    fun buildSimpleDialog(context: Context, resourceTitle: Int): AlertDialog.Builder{
        return AlertDialog.Builder(context)
            .setTitle(context.getString(resourceTitle))
    }

    fun showSimpleDialog(context: Context, resourceTitle: Int, resourceMessage: Int){
        buildSimpleDialog(context, resourceTitle, resourceMessage).show()
    }
}