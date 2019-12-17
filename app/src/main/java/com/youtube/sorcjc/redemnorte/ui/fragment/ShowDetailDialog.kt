package com.youtube.sorcjc.redemnorte.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.squareup.picasso.Picasso
import com.youtube.sorcjc.redemnorte.BuildConfig
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.model.Item
import com.youtube.sorcjc.redemnorte.util.*
import kotlinx.android.synthetic.main.dialog_view_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class ShowDetailDialog : DialogFragment(), Callback<Item>, View.OnClickListener {

    // Location of the last photo taken
    private var currentPhotoPath: String? = null

    // Params required for the request
    private var sheetId: Int = -1
    private var itemId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_view_detail, container, false)

        setHasOptionsMenu(true)

        productDataByQrCode
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = getString(R.string.title_item_show)

        val appCompatActivity = (activity as AppCompatActivity)
        appCompatActivity.setSupportActionBar(toolbar)

        appCompatActivity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }

        btnCapturePhoto.setOnClickListener(this)
    }

    private val productDataByQrCode: Unit
        get() {
            val call = MyApiAdapter.getApiService().getItem(itemId)
            call.enqueue(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            sheetId = it.getInt("hoja_id")
            itemId = it.getInt("item_id")
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
        etQR.setText(item.inventory_code)
        etPatrimonial.setText(item.patrimonial)
        etOldCode.setText(item.old_code)
        spinnerOldYear.setSelection(spinnerOldYear.getItemIndex(item.old_year))
        etPreservation.setText(context?.let { item.getStatusText(it) })
        checkOperative.isChecked = item.operative
        checkLabeled.isChecked = item.labeled
        etDescription.setText(item.denomination)
        etColor.setText(item.color)
        etBrand.setText(item.brand)
        etModel.setText(item.model)
        etSeries.setText(item.series)
        etLong.setText(item.length)
        etWidth.setText(item.width)
        etHeight.setText(item.height)
        etObservation.setText(item.observation)

        loadDetailPhoto(item)
    }

    override fun onResponse(call: Call<Item>, response: Response<Item>) {
        if (response.isSuccessful) {
            response.body()?.let {
                setProductDataToViews(it)
            }
        } else {
            context?.toast(getString(R.string.error_format_server_response))
        }

        showDialogContent()
    }

    override fun onFailure(call: Call<Item>, t: Throwable) {
        context?.toast(t.localizedMessage ?: "")

        showDialogContent()
    }

    private fun showDialogContent() {
        progressBarDetail.visibility = View.GONE

        scrollViewShowDetail.visibility = View.VISIBLE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCapturePhoto -> capturePhotoIfPermissionIsGranted()
        }
    }

    private fun capturePhotoIfPermissionIsGranted() {
        activity?.checkAndRequestPermission(
            getString(R.string.dialog_storage_title),
            getString(R.string.dialog_storage_explanation),
            Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_STORAGE_PERMISSION,
            ::capturePhoto
        )
    }

    private fun capturePhoto() {
        // Create a File for the item photo
        val photoFile: File?

        photoFile = try {
            createDestinationFile()
        } catch (ex: IOException) {
            return
        }

        // Continue only if the File was successfully created
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val photoURI = context?.let {
            FileProvider.getUriForFile(it,  BuildConfig.APPLICATION_ID + ".provider", photoFile)
        }

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
    }

    @Throws(IOException::class)
    private fun createDestinationFile(): File { // Path for the temporary image and its name
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFileName = System.currentTimeMillis().toString()
        val image = File.createTempFile(
                imageFileName,  // prefix
                ".$DEFAULT_PHOTO_EXTENSION",  // suffix
                storageDirectory // directory
        )

        // Save a the file path
        currentPhotoPath = image.absolutePath
        // Log.d("ShowDetailDialog", "currentPhotoPath $currentPhotoPath")

        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                // Log.d("ShowDetailDialog", "REQUEST_CODE_CAMERA = $REQUEST_CODE_CAMERA")
                onCaptureImageResult()
            }
        }
    }

    private fun onCaptureImageResult() {
        val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        postPicture(bitmap)

        currentPhotoPath?.let {
            val deleted = File(it).delete()
            if (!deleted) {
                context?.toast(getString(R.string.item_photo_can_be_deleted_from_phone))
            }
        }

    }

    private fun postPicture(bitmap: Bitmap, extension: String = DEFAULT_PHOTO_EXTENSION) {
        val base64 = bitmap.getBase64()

        val call = MyApiAdapter.getApiService()
                .postPhoto(itemId, base64, extension)

        call.enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    context?.toast(getString(R.string.item_photo_success))
                    response.body()?.let { loadDetailPhoto(it) }
                } else {
                    activity?.toast(getString(R.string.item_photo_error))
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                context?.toast(t.localizedMessage ?: "")
            }
        })
    }

    private fun loadDetailPhoto(item: Item) {
        Picasso.with(context)
                .load(item.photoUrl())
                .fit().centerCrop().into(ivPhoto)

        btnCapturePhoto.text = getString(R.string.btn_replace_item_photo)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_STORAGE_PERMISSION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    context?.toast(getString(R.string.permission_storage_granted))
                } else {
                    context?.toast(getString(R.string.permission_storage_denied))
                }
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 10100
        private const val REQUEST_CODE_CAMERA = 10101

        @JvmStatic
        fun newInstance(sheetId: Int, itemId: Int): ShowDetailDialog {
            val args = Bundle()
            args.putInt("hoja_id", sheetId)
            args.putInt("item_id", itemId)

            val f = ShowDetailDialog()
            f.arguments = args
            return f
        }

        private const val DEFAULT_PHOTO_EXTENSION = "jpg"
    }
}