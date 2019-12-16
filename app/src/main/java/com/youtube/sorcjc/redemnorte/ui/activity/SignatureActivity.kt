package com.youtube.sorcjc.redemnorte.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.io.MyApiService
import com.youtube.sorcjc.redemnorte.io.response.PublicDataResponse
import com.youtube.sorcjc.redemnorte.model.ResponsibleUser
import com.youtube.sorcjc.redemnorte.ui.view.CaptureSignatureView
import com.youtube.sorcjc.redemnorte.util.arrayAdapter
import com.youtube.sorcjc.redemnorte.util.getBase64
import com.youtube.sorcjc.redemnorte.util.toast
import kotlinx.android.synthetic.main.activity_signature.*
import kotlinx.android.synthetic.main.item_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignatureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        val mContent = findViewById<LinearLayout>(R.id.linearLayout)
        val mSig = CaptureSignatureView(this, null)
        mContent.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        fetchPublicData()

        btnSignatureClear.setOnClickListener {
            mSig.clearCanvas()
        }

        btnSignatureSave.setOnClickListener {
            enableButtons(false)

            registerSignature(mSig.bitmap)
        }
    }

    private fun registerSignature(bitmap: Bitmap) {
        val base64 = bitmap.getBase64()
        val responsibleName = atvResponsible.text.toString().trim()

        val call = MyApiAdapter.getApiService()
                .postSignature(responsibleId, responsibleName, base64)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast(getString(R.string.signature_upload_success))
                } else {
                    toast(getString(R.string.signature_upload_error))
                    enableButtons()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage ?: "")
                enableButtons()
            }
        })
    }

    private fun enableButtons(enabled: Boolean = true) {
        btnSignatureSave.isEnabled = enabled
        btnSignatureClear.isEnabled = enabled
    }

    private fun fetchPublicData() {
        val call = MyApiAdapter.getApiService().getPublicData()
        call.enqueue(PublicDataCallback())
    }

    internal inner class PublicDataCallback : Callback<PublicDataResponse> {
        override fun onResponse(call: Call<PublicDataResponse>, response: Response<PublicDataResponse>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    populateAutoComplete(it)
                }
            } else {
                toast(getString(R.string.error_format_server_response))
            }

            // showDialogContent()
        }

        override fun onFailure(call: Call<PublicDataResponse>, t: Throwable) {
            toast(t.localizedMessage ?: "")

            // showDialogContent()
        }
    }

    private var responsibleId = -1

    private fun populateAutoComplete(publicDataResponse: PublicDataResponse) {
        atvResponsible.setAdapter(arrayAdapter(publicDataResponse.responsibleUsers))

        atvResponsible.onItemClickListener = AdapterView.OnItemClickListener{
            parent, _, position, _ ->

            responsibleId = (parent.getItemAtPosition(position) as ResponsibleUser).id
        }
    }
}
