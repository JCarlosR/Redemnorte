package com.youtube.sorcjc.redemnorte.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class MessageDialogFragment extends DialogFragment {
    public interface MessageDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNeutralClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message, MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle);

        builder.setPositiveButton("Usar código", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {
                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                }
            }
        });
        builder.setNeutralButton("Volver a tomar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {
                    mListener.onDialogNeutralClick(MessageDialogFragment.this);
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {
                    mListener.onDialogNegativeClick(MessageDialogFragment.this);
                }
            }
        });

        return builder.create();
    }
}