package com.youtube.sorcjc.redemnorte.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import com.youtube.sorcjc.redemnorte.Global
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
import com.youtube.sorcjc.redemnorte.util.toast
import com.youtube.sorcjc.redemnorte.util.showInfoDialog
import kotlinx.android.synthetic.main.dialog_new_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDialogFragment : DialogFragment(), View.OnClickListener {

    // Param that contains the ID of the parent header
    private var sheetId: String? = null

    // The next param only is provided when the fragment is opened in edit mode
    private var qr_code_param: String? = null

    // Responsible associated with the header, so we can check if the detail is assigned to it
    // in old databases
    private var responsable: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sheetId = arguments?.getString("hoja_id")
        qr_code_param = arguments?.getString("qr_code")
        responsable = arguments?.getString("responsable")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dialog_new_detail, container, false)

        var title = "Registrar nuevo bien"
        if (qr_code_param != null && qr_code_param!!.isNotEmpty())
            title = "Editar bien seleccionado"

        toolbar.title = title

        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel)
        }
        setHasOptionsMenu(true)

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
        setupEditMode()

        return view
    }

    private fun setupEditMode() {
        // qr_code_param provided => edit mode
        if (qr_code_param!!.isNotEmpty()) {
            etQR.isEnabled = false
            btnCaptureQR.visibility = View.GONE
            btnCheckQR.visibility = View.GONE
            val call = MyApiAdapter.getApiService().getItem(sheetId, qr_code_param)
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
        etQR.setText(item.qr)
        etPatrimonial.setText(item.patrimonial)
        etOldCode.setText(item.old_code)

        spinnerOldYear.setSelection(Global.getSpinnerIndex(spinnerOldYear, item.old_year))
        spinnerPreservation.setSelection(Global.getSpinnerIndex(spinnerPreservation, item.preservation))

        checkOperative.isChecked = item.isOperative == "S"
        checkEtiquetado.isChecked = item.etiquetado == "1"

        etDescription.setText(item.description)
        etColor.setText(item.color)
        etBrand.setText(item.brand)
        etModel.setText(item.model)
        etSeries.setText(item.series)
        etDimLong.setText(item.dimLong)
        etDimWidth.setText(item.dimWidth)
        etDimHigh.setText(item.dimHigh)
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
        val QR_code = etQR.text.toString().trim()
        val patrimonial_code = etPatrimonial.text.toString().trim()
        val old_code = etOldCode.text.toString().trim()
        val old_year = spinnerOldYear.selectedItem.toString()
        val denominacion = etDescription.text.toString().trim()
        val marca = etBrand.text.toString().trim()
        val modelo = etModel.text.toString().trim()
        val serie = etSeries.text.toString().trim()
        val color = etColor.text.toString().trim()
        val largo = etDimLong.text.toString().trim()
        val ancho = etDimWidth.text.toString().trim()
        val alto = etDimHigh.text.toString().trim()
        val condicion = spinnerPreservation.selectedItem.toString()
        val etiquetado = if (checkEtiquetado.isChecked) "1" else "0"
        val operativo = if (checkOperative.isChecked) "S" else "N"
        val observacion = etObservation.text.toString().trim()

        val call: Call<SimpleResponse>
        call = if (qr_code_param != null && qr_code_param!!.isNotEmpty()) { // Qr code provided => edit mode
            MyApiAdapter.getApiService().updateItem(
                    sheetId, QR_code, patrimonial_code, old_code, old_year,
                    denominacion, marca, modelo, serie, color,
                    largo, ancho, alto,
                    condicion, etiquetado, operativo, observacion
            )
        } else { // Qr code assigned => register new detail
            MyApiAdapter.getApiService().storeItem(
                    sheetId, QR_code, patrimonial_code, old_code, old_year,
                    denominacion, marca, modelo, serie, color,
                    largo, ancho, alto,
                    condicion, etiquetado, operativo, observacion
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
            R.id.btnCaptureQR -> {
                val intentQR = Intent(context, SimpleScannerActivity::class.java)
                startActivityForResult(intentQR, 1)
            }
            R.id.btnCapturePatrimonial -> {
                val intentPatrimonial = Intent(context, SimpleScannerActivity::class.java)
                startActivityForResult(intentPatrimonial, 2)
            }
            R.id.btnCaptureOldCode -> {
                val intentCaptureOld = Intent(context, SimpleScannerActivity::class.java)
                startActivityForResult(intentCaptureOld, 3)
            }
            R.id.btnCheckQR -> performCheckQrRequest()
            R.id.btnTakeByPatrimonial -> performByPatrimonialRequest()
            R.id.btnTakeByOldCode -> performByOldCodeRequest()
        }
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
        spinnerPreservation.setSelection(Global.getSpinnerIndex(spinnerPreservation, preservation))

        val empleado = bienConsolidado.empleado!!.trim()
        if (empleado != responsable) {
            var message = "Este bien le pertenece al usuario $empleado.\n"
            message += "Verifica si el responsable de esta hoja $responsable se hará cargo.\n"
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
                    etDescription!!.setText(byOldCodeResponse.item.description)
                    etPatrimonial!!.setText(byOldCodeResponse.item.codigoActivo)
                    context?.toast(byOldCodeResponse.message)
                }
            }
        }

        override fun onFailure(call: Call<ByOldCodeResponse?>, t: Throwable) {
            context?.toast(t.localizedMessage)
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
            context?.toast(t.localizedMessage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data.getStringExtra("code")
                etQR!!.setText(result)
                context?.toast("Usa el botón del ojito para verificar que no se repita el QR.")
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data.getStringExtra("code")
                etPatrimonial!!.setText(result)
                context?.toast("Usa el botón de la diana para buscar y traer datos.")
            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data.getStringExtra("code")
                etOldCode!!.setText(result)
                context?.toast("Usa el botón de la diana para buscar y traer datos.")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(hoja_id: String?, qr_code: String?, responsable: String?): DetailDialogFragment {
            val f = DetailDialogFragment()
            val args = Bundle()
            args.putString("hoja_id", hoja_id)
            args.putString("qr_code", qr_code)
            args.putString("responsable", responsable)
            f.arguments = args
            return f
        }
    }
}