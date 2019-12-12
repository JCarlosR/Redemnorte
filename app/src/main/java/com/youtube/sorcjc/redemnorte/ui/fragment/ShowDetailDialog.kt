package com.youtube.sorcjc.redemnorte.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.squareup.picasso.Picasso
import com.youtube.sorcjc.redemnorte.Global
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.io.response.BienResponse
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse
import com.youtube.sorcjc.redemnorte.model.Item
import com.youtube.sorcjc.redemnorte.util.getBase64
import com.youtube.sorcjc.redemnorte.util.getItemIndex
import com.youtube.sorcjc.redemnorte.util.toast
import kotlinx.android.synthetic.main.dialog_view_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class ShowDetailDialog : DialogFragment(), Callback<BienResponse>, View.OnClickListener {
    private val DEFAULT_PHOTO_EXTENSION = "jpg"

    // Location of the last photo taken
    private var currentPhotoPath: String? = null

    // Params required for the request
    private var sheetId: String = ""
    private var qrCode: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_view_detail, container, false)

        toolbar.title = getString(R.string.title_item_show)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }
        setHasOptionsMenu(true)

        btnCapturePhoto.setOnClickListener(this)

        productDataByQrCode
        return view
    }

    private val productDataByQrCode: Unit
        get() {
            val call = MyApiAdapter.getApiService().getItem(sheetId, qrCode)
            call.enqueue(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            qrCode = it.getString("qr_code", "")
            sheetId = it.getString("hoja_id", "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // close button
            dismiss()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setProductDataToViews(item: Item) {
        etQR.setText(item.qr)
        etPatrimonial.setText(item.patrimonial)
        etOldCode.setText(item.old_code)
        spinnerOldYear.setSelection(spinnerOldYear.getItemIndex(item.old_year))
        etPreservation.setText(item.preservation)
        checkOperative.isChecked = item.isOperative == "S"
        checkEtiquetado.isChecked = item.etiquetado?.trim() == "1"
        etDescription.setText(item.description)
        etColor.setText(item.color)
        etBrand.setText(item.brand)
        etModel.setText(item.model)
        etSeries.setText(item.series)
        etDimLong.setText(item.dimLong)
        etDimWidth.setText(item.dimWidth)
        etDimHigh.setText(item.dimHigh)
        etObservation.setText(item.observation)

        val extension = item.photo_extension
        if (extension != null && extension.isNotEmpty()) {
            loadDetailPhoto(extension)
        }
    }

    override fun onResponse(call: Call<BienResponse>, response: Response<BienResponse>) {
        if (response.isSuccessful) {
            val item = response.body()!!.item
            setProductDataToViews(item)
        } else {
            context?.toast(getString(R.string.error_format_server_response))
        }
    }

    override fun onFailure(call: Call<BienResponse>, t: Throwable) {
        context?.toast(t.localizedMessage)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCapturePhoto -> capturePhoto()
        }
    }

    private fun capturePhoto() { // Create the File where the photo should go
        val photoFile: File?

        photoFile = try {
            createDestinationFile()
        } catch (ex: IOException) {
            return
        }

        // Continue only if the File was successfully created
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
        startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
    }

    @Throws(IOException::class)
    private fun createDestinationFile(): File { // Path for the temporary image and its name
        val storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        val imageFileName = "" + System.currentTimeMillis()
        val image = File.createTempFile(
                imageFileName,  // prefix
                ".$DEFAULT_PHOTO_EXTENSION",  // suffix
                storageDirectory // directory
        )
        // Save a the file path
        currentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA)
                onCaptureImageResult(data)
        }
    }

    private fun onCaptureImageResult(data: Intent) {
        val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        postPicture(bitmap)

        currentPhotoPath?.let {
            val deleted = File(it).delete()
            if (!deleted) {
                context?.toast("Si desea luego puede eliminar la foto del celular")
            }
        }

    }

    private fun postPicture(bitmap: Bitmap, extension: String = DEFAULT_PHOTO_EXTENSION) {
        val base64 = bitmap.getBase64()
        val call = MyApiAdapter.getApiService()
                .postPhoto(base64, extension, sheetId, qrCode)
        call.enqueue(object : Callback<SimpleResponse?> {
            override fun onResponse(call: Call<SimpleResponse?>, response: Response<SimpleResponse?>) {
                if (response.isSuccessful) {
                    context?.toast("La foto se ha subido correctamente")
                    loadDetailPhoto(DEFAULT_PHOTO_EXTENSION)
                } else {
                    activity?.toast("Ocurrió un problema al enviar la imagen")
                }
            }

            override fun onFailure(call: Call<SimpleResponse?>, t: Throwable) {
                context?.toast(t.localizedMessage)
            }
        })
    }

    private fun loadDetailPhoto(extension: String) {
        val imageUrl = Global.getProductPhotoUrl(sheetId, qrCode, extension)
        Picasso.with(context).load(imageUrl).fit().centerCrop().into(ivPhoto)
        btnCapturePhoto.text = getString(R.string.btn_replace_item_photo)
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 10101

        @JvmStatic
        fun newInstance(sheetId: String?, qrCode: String?): ShowDetailDialog {
            val f = ShowDetailDialog()
            val args = Bundle()
            args.putString("hoja_id", sheetId)
            args.putString("qr_code", qrCode)
            f.arguments = args
            return f
        }
    }
}