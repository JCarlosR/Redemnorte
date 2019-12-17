package com.youtube.sorcjc.redemnorte.util

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showInfoDialog(
        title: String, message: String,
        positiveBtn: String = "Ok", action: (()->Unit)? = null
) {
    val alertDialog = AlertDialog.Builder(this).create()
    alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveBtn) { dialog, _ ->
        action?.invoke() // invoke an action in case it exists
        dialog.dismiss()
    }
    alertDialog.show()
}

fun Context.showConfirmDialog(
        title: String, message: String,
        positiveBtn: String = "Ok", negativeBtn: String = "Cancel",
        actionIfAgree: () -> Unit
) {
    val alertDialog = AlertDialog.Builder(this).create()
    alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeBtn) { dialog, _ ->
        dialog.dismiss()
    }
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveBtn) { dialog, _ ->
        actionIfAgree()
        dialog.dismiss()
    }
    alertDialog.show()
}

fun <T> Context.arrayAdapter(objects: List<T>): ArrayAdapter<T> {
    return ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, objects)
}