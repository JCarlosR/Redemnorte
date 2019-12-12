package com.youtube.sorcjc.redemnorte.util

import android.widget.Spinner

fun Spinner.getItemIndex(myString: String?): Int {
    var index = -1

    for (i in 0 until count)
        if (getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
            index = i
            break
        }

    return index
}