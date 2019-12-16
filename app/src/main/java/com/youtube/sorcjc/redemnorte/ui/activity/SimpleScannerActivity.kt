package com.youtube.sorcjc.redemnorte.ui.activity

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.ui.fragment.MessageDialog
import com.youtube.sorcjc.redemnorte.ui.fragment.MessageDialog.MessageDialogListener
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class SimpleScannerActivity : AppCompatActivity(), ZBarScannerView.ResultHandler, MessageDialogListener {
    private val mScannerView by lazy {
        ZBarScannerView(this)
    }

    private var code: String? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        // Set the scanner view as the content view
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()

        // Register this class as handler for results
        mScannerView.setResultHandler(this)

        // Start camera on resume
        mScannerView.startCamera()
    }

    public override fun onPause() {
        super.onPause()

        // Stop camera on pause
        mScannerView.stopCamera()
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
        val fragment: DialogFragment = MessageDialog.newInstance(getString(R.string.scanner_results), message, this)
        fragment.show(supportFragmentManager, "")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment?) {
        // Toast.makeText(this, "Code to use => " + code, Toast.LENGTH_SHORT).show();

        val data = Intent()
        data.putExtra("code", code)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onDialogNeutralClick(dialog: DialogFragment?) {
        // Resume the camera
        mScannerView.resumeCameraPreview(this)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment?) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object {
        private const val TAG = "ScannerLog"
    }
}