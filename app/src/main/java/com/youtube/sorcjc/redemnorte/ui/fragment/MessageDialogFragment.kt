package com.youtube.sorcjc.redemnorte.ui.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.ui.fragment.MessageDialogFragment

class MessageDialogFragment : DialogFragment() {
    interface MessageDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment?)
        fun onDialogNeutralClick(dialog: DialogFragment?)
        fun onDialogNegativeClick(dialog: DialogFragment?)
    }

    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mListener: MessageDialogListener? = null
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage(mMessage)
                .setTitle(mTitle)
        builder.setPositiveButton(getString(R.string.btn_positive_use_code)) { dialog, id ->
            if (mListener != null) {
                mListener!!.onDialogPositiveClick(this@MessageDialogFragment)
            }
        }
        builder.setNeutralButton(getString(R.string.btn_neutral_take_again)) { dialog, id ->
            if (mListener != null) {
                mListener!!.onDialogNeutralClick(this@MessageDialogFragment)
            }
        }
        builder.setNegativeButton(getString(R.string.btn_negative_cancel)) { dialog, id ->
            if (mListener != null) {
                mListener!!.onDialogNegativeClick(this@MessageDialogFragment)
            }
        }
        return builder.create()
    }

    companion object {
        fun newInstance(title: String?, message: String?, listener: MessageDialogListener?): MessageDialogFragment {
            val fragment = MessageDialogFragment()
            fragment.mTitle = title
            fragment.mMessage = message
            fragment.mListener = listener
            return fragment
        }
    }
}