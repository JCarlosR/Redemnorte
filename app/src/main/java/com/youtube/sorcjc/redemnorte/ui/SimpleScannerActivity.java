package com.youtube.sorcjc.redemnorte.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.ui.fragment.MessageDialogFragment;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler, MessageDialogFragment.MessageDialogListener {

    private final static String TAG = "ScannerLog";
    private ZBarScannerView mScannerView;

    private String code;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this); // Programmatically initialize the scanner view
        setContentView(mScannerView);             // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera(); // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Scan results
        code = rawResult.getContents();
        // Scan format (qr code, pdf417 etc.)
        final String format = rawResult.getBarcodeFormat().getName();
        // Concat
        final String fullMessage = "Contents = "+code+", Format = "+format;

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        showMessageDialog(fullMessage);
    }

    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Resultados", message, this);
        fragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Toast.makeText(this, "Código a usar => " + code, Toast.LENGTH_SHORT).show();

        final Intent data = new Intent();
        data.putExtra("code", code);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public void onDialogNeutralClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }


}