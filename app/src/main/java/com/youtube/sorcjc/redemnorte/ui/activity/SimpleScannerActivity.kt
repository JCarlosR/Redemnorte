package com.youtube.sorcjc.redemnorte.ui.activity

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.ui.fragment.MessageDialogFragment
import com.youtube.sorcjc.redemnorte.ui.fragment.MessageDialogFragment.MessageDialogListener
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class SimpleScannerActivity : AppCompatActivity(), ZBarScannerView.ResultHandler, MessageDialogListener {
    private val mScannerView by lazy {
        ZBarScannerView(this) // Programmatically initialize the scanner view
    }

    private var code: String? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(mScannerView) // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView.startCamera() // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView.stopCamera() // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        // Scan results
        code = rawResult.contents

        // Scan format (qr code, pdf417 etc.)
        val format = rawResult.barcodeFormat.name

        // Concat
        val fullMessage = "Contents = $code, Format = $format"

        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
        }

        showMessageDialog(fullMessage)
    }

    private fun showMessageDialog(message: String?) {
        val fragment: DialogFragment = MessageDialogFragment.newInstance(getString(R.string.scanner_results), message, this)
        fragment.show(supportFragmentManager, "")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        // Toast.makeText(this, "Código a usar => " + code, Toast.LENGTH_SHORT).show();

        val data = Intent()
        data.putExtra("code", code)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        // Resume the camera
        mScannerView.resumeCameraPreview(this)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object {
        private const val TAG = "ScannerLog"
    }
}