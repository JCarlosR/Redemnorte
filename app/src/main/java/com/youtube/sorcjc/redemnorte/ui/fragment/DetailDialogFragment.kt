package com.youtube.sorcjc.redemnorte.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.io.response.BienResponse
import com.youtube.sorcjc.redemnorte.io.response.ByOldCodeResponse
import com.youtube.sorcjc.redemnorte.io.response.ByPatrimonialResponse
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse
import com.youtube.sorcjc.redemnorte.model.BienConsolidado
import com.youtube.sorcjc.redemnorte.model.Item
import com.youtube.sorcjc.redemnorte.ui.activity.DetailsActivity
import com.youtube.sorcjc.redemnorte.ui.activity.SimpleScannerActivity
import com.youtube.sorcjc.redemnorte.util.getItemIndex
import com.youtube.sorcjc.redemnorte.util.showConfirmDialog
import com.youtube.sorcjc.redemnorte.util.showInfoDialog
import com.youtube.sorcjc.redemnorte.util.toast
import kotlinx.android.synthetic.main.dialog_new_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDialogFragment : DialogFragment(), View.OnClickListener {

    // Parent header
    private var sheetId: Int = -1

    // Only provided in edit mode
    private var qrCodeParam: String? = null

    // Responsible associated with the header, so we can check if the detail is assigned to it
    // in old databases
    private var responsible: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            sheetId = it.getInt("hoja_id")
            qrCodeParam = it.getString("qr_code")
            responsible = it.getString("responsable")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dialog_new_detail, container, false)

        setHasOptionsMenu(true)

        setupEditMode()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var title = getString(R.string.title_item_create)

        qrCodeParam?.let {
            title = getString(R.string.title_item_edit)
        }

        toolbar.title = title

        val appCompatActivity = (activity as AppCompatActivity?)
        appCompatActivity?.setSupportActionBar(toolbar)

        appCompatActivity?.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_close)
        }

        registerListeners()
    }

    private fun registerListeners()
    {
        // Capture QR, Patrimonial Barcode & Old code
        btnCaptureQR.setOnClickListener(this)
        btnCapturePatrimonial.setOnClickListener(this)
        btnCaptureOldCode.setOnClickListener(this)

        // Check if QR is available
        btnCheckQR.setOnClickListener(this)

        // Take data by patrimonial code
        btnTakeByPatrimonial.setOnClickListener(this)

        // Take data by old code
        btnTakeByOldCode.setOnClickListener(this)

    }

    private fun setupEditMode() {
        // qr_code_param provided => edit mode
        if (qrCodeParam?.isNotEmpty() == true) {
            etQR.isEnabled = false
            btnCaptureQR.visibility = View.GONE
            btnCheckQR.visibility = View.GONE

            val call = MyApiAdapter.getApiService().getItem(sheetId, qrCodeParam)
            call.enqueue(GetPreviousDataCallback())
        }
    }

    internal inner class GetPreviousDataCallback : Callback<BienResponse> {
        override fun onResponse(call: Call<BienResponse>, response: Response<BienResponse>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    setProductDataToViews(it.item)
                }

            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<BienResponse>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }
    }

    private fun setProductDataToViews(item: Item) {
        etQR.setText(item.inventory_code)
        etPatrimonial.setText(item.patrimonial)
        etOldCode.setText(item.old_code)

        spinnerOldYear.setSelection(spinnerOldYear.getItemIndex(item.old_year))
        spinnerPreservation.setSelection(spinnerPreservation.getItemIndex(item.status))

        checkOperative.isChecked = item.operative
        checkEtiquetado.isChecked = item.labeled

        etDescription.setText(item.denomination)
        etColor.setText(item.color)
        etBrand.setText(item.brand)
        etModel.setText(item.model)
        etSeries.setText(item.series)
        etDimLong.setText(item.length)
        etDimWidth.setText(item.width)
        etDimHigh.setText(item.height)
        etObservation.setText(item.observation)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.save) {
            validateForm()
            return true
        } else if (id == android.R.id.home) {
            // close button
            dismiss()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validateForm() {
        if (!validateEditText(etQR, tilQR, R.string.error_til_qr)) {
            return
        }
        if (!validateEditText(etDescription, tilDescription, R.string.error_til_description)) {
            return
        }
        performRegisterRequest()
    }

    private fun performRegisterRequest() {
        val qrCode = etQR.text.toString().trim()
        val patrimonial = etPatrimonial.text.toString().trim()
        val oldCode = etOldCode.text.toString().trim()
        val oldYear = spinnerOldYear.selectedItem.toString()
        val denomination = etDescription.text.toString().trim()
        val brand = etBrand.text.toString().trim()
        val model = etModel.text.toString().trim()
        val series = etSeries.text.toString().trim()
        val color = etColor.text.toString().trim()
        val length = etDimLong.text.toString().trim()
        val width = etDimWidth.text.toString().trim()
        val height = etDimHigh.text.toString().trim()
        val status = spinnerPreservation.selectedItem.toString()
        val labeled = checkEtiquetado.isChecked
        val operative = checkOperative.isChecked
        val observation = etObservation.text.toString().trim()

        val call: Call<SimpleResponse>
        call = if (qrCodeParam != null && qrCodeParam!!.isNotEmpty()) { // Qr code provided => edit mode
            MyApiAdapter.getApiService().updateItem(
                    sheetId, qrCode, patrimonial, oldCode, oldYear,
                    denomination, brand, model, series, color,
                    length, width, height,
                    status, labeled, operative, observation
            )
        } else { // Qr code assigned => register new detail
            MyApiAdapter.getApiService().storeItem(
                    sheetId, qrCode, patrimonial, oldCode, oldYear,
                    denomination, brand, model, series, color,
                    length, width, height,
                    status, labeled, operative, observation
            )
        }
        call.enqueue(CreateItemCallback())
    }

    internal inner class CreateItemCallback : Callback<SimpleResponse> {
        override fun onResponse(call: Call<SimpleResponse>, response: Response<SimpleResponse>) {
            if (response.isSuccessful) {
                if (response.body()!!.isError) {
                    // Just show an error message.
                    context?.toast(response.body()!!.message)
                } else {
                    // Show a successful message,
                    context?.toast(response.body()!!.message)
                    // re-load the recyclerView,
                    (activity as DetailsActivity?)!!.loadItems()
                    // and dismiss this dialog.
                    dismiss()
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }
    }

    private fun validateEditText(editText: EditText?, textInputLayout: TextInputLayout?, errorString: Int): Boolean {
        if (editText!!.text.toString().trim().isEmpty()) {
            textInputLayout!!.error = getString(errorString)
            return false
        } else {
            textInputLayout!!.isErrorEnabled = false
        }
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCaptureQR -> startScannerFor(REQUEST_QR_CODE)
            R.id.btnCapturePatrimonial -> startScannerFor(REQUEST_PATRIMONIAL_CODE)
            R.id.btnCaptureOldCode -> startScannerFor(REQUEST_OLD_CODE)

            R.id.btnCheckQR -> performCheckQrRequest()
            R.id.btnTakeByPatrimonial -> performByPatrimonialRequest()
            R.id.btnTakeByOldCode -> performByOldCodeRequest()
        }
    }

    private fun startScannerFor(requestCode: Int) {
        activity?.let { context?.let { ctx -> checkCameraPermission(ctx, it, requestCode) } }
    }

    private fun checkCameraPermission(context: Context, activity: Activity, requestCode: Int) {
        val cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        /*
        Log.d("DetailDialogFragment", "context $context")
        Log.d("DetailDialogFragment", "cameraPermission $cameraPermission")
        Log.d("DetailDialogFragment", "PackageManager.PERMISSION_DENIED ${PackageManager.PERMISSION_DENIED}")
        Log.d("DetailDialogFragment", "activity $activity")
        */

        if (cameraPermission == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                // Show an explanation to the user.
                // After the user sees the explanation, try to request the permission.
                context.showConfirmDialog(getString(R.string.dialog_camera_title), getString(R.string.dialog_camera_explanation)) {
                        requestCameraPermission(activity)
                }
            } else {
                // No explanation needed, we can request the permission.
                requestCameraPermission(activity)
            }
        } else {
            val intentQR = Intent(context, SimpleScannerActivity::class.java)
            startActivityForResult(intentQR, requestCode)
        }
    }

    private fun requestCameraPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        Log.d("DetailDialogFragment", "requestPermissions called")
    }

    private fun performByPatrimonialRequest() {
        val call = MyApiAdapter.getApiService().getByPatrimonial(etPatrimonial!!.text.toString().trim())
        call.enqueue(TakeByPatrimonialCallback())
    }

    internal inner class TakeByPatrimonialCallback : Callback<ByPatrimonialResponse?> {
        override fun onResponse(call: Call<ByPatrimonialResponse?>, response: Response<ByPatrimonialResponse?>) {
            if (response.isSuccessful) {
                val byPatrimonialResponse = response.body()
                // The message is used for both, successful and error responses
                if (byPatrimonialResponse!!.isError) {
                    context?.toast(byPatrimonialResponse.message)
                } else {
                    setBienConsolidadoInViews(byPatrimonialResponse.bienConsolidado)
                    context?.toast(byPatrimonialResponse.message)
                }
            }
        }

        override fun onFailure(call: Call<ByPatrimonialResponse?>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }
    }

    private fun setBienConsolidadoInViews(bienConsolidado: BienConsolidado?) {
        if (bienConsolidado == null) return
        val description = bienConsolidado.description.trim()
        val brand = bienConsolidado.brand!!.trim()
        val model = bienConsolidado.model!!.trim()
        val series = bienConsolidado.series!!.trim()
        val estado = bienConsolidado.estado
        val situacion = bienConsolidado.situacion!!.trim()

        // final String ubicacion = bienConsolidado.getUbicacion();
        // final String local = bienConsolidado.getLocal();
        // WHERE situacion =  'BP' OR situacion =  'BA' OR situacion =  'NO' OR situacion =  'NU'

        if (situacion == "BP" || situacion == "BA" || situacion == "NO" || situacion == "NU") {
            val title = "Importante"
            val message = "Lamentablemente este bien ha sido dado de baja."
            context?.showInfoDialog(title, message)
            return
        }
        etDescription.setText(description)
        etBrand.setText(brand)
        etModel.setText(model)
        etSeries.setText(series)

        var preservation = ""
        when (estado) {
            "BU" -> preservation = "Bueno"
            "SE" -> preservation = "Bueno"
            "RE" -> preservation = "Regular"
            "MA" -> preservation = "Malo"
            "PB" -> preservation = "Malo"
            "ER" -> preservation = "Malo"
            "IN" -> preservation = "Malo"
        }
        spinnerPreservation.setSelection(spinnerPreservation.getItemIndex(preservation))

        val empleado = bienConsolidado.empleado!!.trim()
        if (empleado != responsible) {
            var message = "Este bien le pertenece al usuario $empleado.\n"
            message += "Verifica si el responsable de esta hoja $responsible se hará cargo.\n"
            message += "De caso contrario, crea una nueva hoja, márcala con el estado pendiente y registra allí el bien."
            context?.showInfoDialog("Importante", message)
        }
    }

    private fun performByOldCodeRequest() {
        val year = spinnerOldYear!!.selectedItem.toString()
        val code = etOldCode!!.text.toString().trim()
        val call = MyApiAdapter.getApiService().getByOldCode(year, code)
        call.enqueue(TakeByOldCodeCallback())
    }

    internal inner class TakeByOldCodeCallback : Callback<ByOldCodeResponse?> {
        override fun onResponse(call: Call<ByOldCodeResponse?>, response: Response<ByOldCodeResponse?>) {
            if (response.isSuccessful) {
                val byOldCodeResponse = response.body()
                // The message is used for both, successful and error responses
                if (byOldCodeResponse!!.isError) {
                    context?.toast(byOldCodeResponse.message)
                } else {
                    etDescription!!.setText(byOldCodeResponse.item.denomination)
                    etPatrimonial!!.setText(byOldCodeResponse.item.codigoActivo)
                    context?.toast(byOldCodeResponse.message)
                }
            }
        }

        override fun onFailure(call: Call<ByOldCodeResponse?>, t: Throwable) {
            context?.toast(t.localizedMessage ?: "")
        }
    }

    private fun performCheckQrRequest() {
        val call = MyApiAdapter.getApiService().getCheckQr(etQR.text.toString().trim())
        call.enqueue(CheckRequestCallback())
    }

    internal inner class CheckRequestCallback : Callback<SimpleResponse> {
        override fun onResponse(call: Call<SimpleResponse>, response: Response<SimpleResponse>) {
            if (response.isSuccessful) {
                context?.toast(response.body()!!.message)
            }
        }

        override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
            context?.toast(t.localizedMessage ?: "")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra("code")
                etQR!!.setText(result)
                context?.toast("Usa el botón del ojito para verificar que no se repita el QR.")
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra("code")
                etPatrimonial!!.setText(result)
                context?.toast("Usa el botón de la diana para buscar y traer datos.")
            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra("code")
                etOldCode!!.setText(result)
                context?.toast("Usa el botón de la diana para buscar y traer datos.")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d("DetailDialogFragment", "onRequestPermissionsResult called")
        Log.d("DetailDialogFragment", "requestCode $requestCode")

        when (requestCode) {
            REQUEST_CAMERA_PERMISSION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    context?.toast("Permission Granted!")
                } else {
                    context?.toast("Permission Denied!")
                }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(sheetId: Int, qr_code: String?, responsible: String?): DetailDialogFragment {
            val f = DetailDialogFragment()
            val args = Bundle()
            args.putInt("hoja_id", sheetId)
            args.putString("qr_code", qr_code)
            args.putString("responsable", responsible)
            f.arguments = args
            return f
        }

        const val REQUEST_QR_CODE = 1
        const val REQUEST_PATRIMONIAL_CODE = 2
        const val REQUEST_OLD_CODE = 3

        const val REQUEST_CAMERA_PERMISSION = 101
    }
}