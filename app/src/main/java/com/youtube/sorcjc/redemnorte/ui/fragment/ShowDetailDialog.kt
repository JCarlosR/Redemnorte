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
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter
import com.youtube.sorcjc.redemnorte.io.response.BienResponse
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse
import com.youtube.sorcjc.redemnorte.model.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class ShowDetailDialog : DialogFragment(), Callback<BienResponse>, View.OnClickListener {
    private val DEFAULT_PHOTO_EXTENSION = "jpg"
    // Location of the last photo taken
    private var currentPhotoPath: String? = null
    private var etQR: EditText? = null
    private var etPatrimonial: EditText? = null
    private var etOldCode: EditText? = null
    private var etDescription: EditText? = null
    private var etColor: EditText? = null
    private var etBrand: EditText? = null
    private var etModel: EditText? = null
    private var etSeries: EditText? = null
    private var etDimLong: EditText? = null
    private var etDimWidth: EditText? = null
    private var etDimHigh: EditText? = null
    private var etObservation: EditText? = null
    private var etPreservation: EditText? = null
    private var spinnerOldYear: Spinner? = null
    private var checkOperative: CheckBox? = null
    private var checkEtiquetado: CheckBox? = null
    // Params required for the request
    private var hoja_id: String? = null
    private var qr_code: String? = null
    // Photo
    var btnCapturePhoto: Button? = null
    private var ivPhoto: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_view_detail, container, false)
        val toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Datos del bien seleccionado"
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            // actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true)
        etQR = view.findViewById<View>(R.id.etQR) as EditText
        etPatrimonial = view.findViewById<View>(R.id.etPatrimonial) as EditText
        etOldCode = view.findViewById<View>(R.id.etOldCode) as EditText
        spinnerOldYear = view.findViewById<View>(R.id.spinnerOldYear) as Spinner
        etDescription = view.findViewById<View>(R.id.etDescription) as EditText
        etColor = view.findViewById<View>(R.id.etColor) as EditText
        etBrand = view.findViewById<View>(R.id.etBrand) as EditText
        etModel = view.findViewById<View>(R.id.etModel) as EditText
        etSeries = view.findViewById<View>(R.id.etSeries) as EditText
        etDimLong = view.findViewById<View>(R.id.etDimLong) as EditText
        etDimWidth = view.findViewById<View>(R.id.etDimWidth) as EditText
        etDimHigh = view.findViewById<View>(R.id.etDimHigh) as EditText
        etObservation = view.findViewById<View>(R.id.etObservation) as EditText
        etPreservation = view.findViewById<View>(R.id.etPreservation) as EditText
        checkOperative = view.findViewById<View>(R.id.checkOperative) as CheckBox
        checkEtiquetado = view.findViewById<View>(R.id.checkEtiquetado) as CheckBox
        btnCapturePhoto = view.findViewById<View>(R.id.btnCapturePhoto) as Button
        btnCapturePhoto!!.setOnClickListener(this)
        ivPhoto = view.findViewById<View>(R.id.ivPhoto) as ImageView
        productDataByQrCode
        return view
    }

    private val productDataByQrCode: Unit
        private get() {
            val call = RedemnorteApiAdapter.getApiService().getBien(hoja_id, qr_code)
            call.enqueue(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        qr_code = arguments!!.getString("qr_code")
        hoja_id = arguments!!.getString("hoja_id")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) { // handle close button click here
            dismiss()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setProductDataToViews(item: Item) {
        etQR!!.setText(item.qr)
        etPatrimonial!!.setText(item.patrimonial)
        etOldCode!!.setText(item.old_code)
        spinnerOldYear!!.setSelection(Global.getSpinnerIndex(spinnerOldYear, item.old_year))
        etPreservation!!.setText(item.preservation)
        checkOperative!!.isChecked = item.isOperative == "S"
        checkEtiquetado!!.isChecked = item.etiquetado!!.trim { it <= ' ' } == "1"
        etDescription!!.setText(item.description)
        etColor!!.setText(item.color)
        etBrand!!.setText(item.brand)
        etModel!!.setText(item.model)
        etSeries!!.setText(item.series)
        etDimLong!!.setText(item.dimLong)
        etDimWidth!!.setText(item.dimWidth)
        etDimHigh!!.setText(item.dimHigh)
        etObservation!!.setText(item.observation)
        val extension = item.photo_extension
        if (extension != null && !extension.isEmpty()) {
            loadDetailPhoto(extension)
        }
    }

    override fun onResponse(call: Call<BienResponse>, response: Response<BienResponse>) {
        if (response.isSuccessful) {
            val item = response.body()!!.item
            setProductDataToViews(item)
        } else {
            Toast.makeText(context, "Formato de respuesta incorrecto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<BienResponse>, t: Throwable) {
        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCapturePhoto -> capturePhoto()
        }
    }

    private fun capturePhoto() { // Create the File where the photo should go
        var photoFile: File? = null
        photoFile = try {
            createDestinationFile()
        } catch (ex: IOException) {
            return
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
            startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
        }
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
            if (requestCode == REQUEST_CODE_CAMERA) onCaptureImageResult(data)
        }
    }

    private fun onCaptureImageResult(data: Intent) {
        val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        postPicture(bitmap, DEFAULT_PHOTO_EXTENSION)
        val deleted = File(currentPhotoPath).delete()
        if (!deleted) {
            Toast.makeText(context, "Si desea luego puede eliminar la foto del celular", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postPicture(bitmap: Bitmap, extension: String) {
        val base64 = Global.getBase64FromBitmap(bitmap)
        val call = RedemnorteApiAdapter.getApiService()
                .postPhoto(base64, extension, hoja_id, qr_code)
        call.enqueue(object : Callback<SimpleResponse?> {
            override fun onResponse(call: Call<SimpleResponse?>, response: Response<SimpleResponse?>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "La foto se ha subido correctamente", Toast.LENGTH_SHORT).show()
                    loadDetailPhoto(DEFAULT_PHOTO_EXTENSION)
                } else {
                    Toast.makeText(activity, "Ocurrió un problema al enviar la imagen", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SimpleResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDetailPhoto(extension: String) {
        val imageUrl = Global.getProductPhotoUrl(hoja_id, qr_code, extension)
        Picasso.with(context).load(imageUrl).fit().centerCrop().into(ivPhoto)
        btnCapturePhoto!!.text = "Reemplazar foto"
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 10101
        fun newInstance(hoja_id: String?, qr_code: String?): ShowDetailDialog {
            val f = ShowDetailDialog()
            val args = Bundle()
            args.putString("hoja_id", hoja_id)
            args.putString("qr_code", qr_code)
            f.arguments = args
            return f
        }
    }
}