package com.youtube.sorcjc.redemnorte.util

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.Toast

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showInfoDialog(title: String, message: String) {
    val alertDialog = AlertDialog.Builder(this).create()
    alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok") {
        dialog, _ -> dialog.dismiss()
    }
    alertDialog.show()
}