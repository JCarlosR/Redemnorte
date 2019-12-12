package com.youtube.sorcjc.redemnorte.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.*
import com.youtube.sorcjc.redemnorte.Global
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.io.response.HojaResponse
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse
import com.youtube.sorcjc.redemnorte.model.ResponsibleUser
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.ui.activity.PanelActivity
import kotlinx.android.synthetic.main.dialog_new_header.*
import com.youtube.sorcjc.redemnorte.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HeaderDialogFragment : DialogFragment() {
    private var sheetId: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        arguments?.getString("hoja_id")?.let {
            sheetId = it
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dialog_new_header, container, false)

        fetchResponsibleUsersData()
        // spinnerResponsible = view.findViewById<View>(R.id.spinnerResponsible) as AutoCompleteTextView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title: String
        if (sheetId.isEmpty())
            title = getString(R.string.title_sheet_create)
        else {
            title = getString(R.string.title_sheet_edit)
            fetchHeaderDataFromServer()
            etId.setText(sheetId)
            etId.isEnabled = false
        }

        toolbar.title = title

        val appCompatActivity = (activity as AppCompatActivity)
        appCompatActivity.setSupportActionBar(toolbar)

        val actionBar = appCompatActivity.supportActionBar
        /*
        Log.d("HeaderDialogFragment", "appCompatActivity => $appCompatActivity")
        Log.d("HeaderDialogFragment", "toolbar => $toolbar")
        Log.d("HeaderDialogFragment", "actionBar => $actionBar")
        */

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }

        // set for new headers (for edit mode will be set later)
        if (sheetId.isEmpty()) {
            setCheckPendingOnChangeListener()
        }
    }

    private fun setCheckPendingOnChangeListener() {
        checkPending.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tilObservation.visibility = View.VISIBLE
                Global.showInformationDialog(context, "Observación", "¿Por qué motivo la hoja se ha marcado como pendiente?")
            } else {
                tilObservation.visibility = View.GONE
                etObservation.setText("")
            }
        }
    }

    private fun populateResponsibleUsersSpinner(responsibleUsers: ArrayList<ResponsibleUser>) {
        context?.let {
            val arrayAdapter = ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, responsibleUsers)
            spinnerResponsible.setAdapter(arrayAdapter)
        }
        // ArrayAdapter<Doctor>(this@CreateAppointmentActivity, android.R.layout.simple_list_item_1, doctors)
    }

    private fun fetchResponsibleUsersData() {
        val call = MyApiAdapter.getApiService().getResponsibleUsers()
        call.enqueue(ResponsibleUsersCallback())
    }

    internal inner class ResponsibleUsersCallback : Callback<ArrayList<ResponsibleUser>> {
        override fun onResponse(call: Call<ArrayList<ResponsibleUser>>, response: Response<ArrayList<ResponsibleUser>>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    populateResponsibleUsersSpinner(it)
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<ArrayList<ResponsibleUser>>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        menu?.removeItem(R.id.search)
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
        if (!validEditText(etId, tilId, R.string.error_hoja_id)) {
            return
        }
        if (!validEditText(etLocal, tilLocal, R.string.error_local)) {
            return
        }
        if (!validEditText(etUbicacion, tilUbicacion, R.string.error_ubicacion)) {
            return
        }
        if (!validEditText(etCargo, tilCargo, R.string.error_cargo)) {
            return
        }
        if (!validEditText(etOficina, tilOficina, R.string.error_oficina)) {
            return
        }
        if (!validEditText(etAmbiente, tilAmbiente, R.string.error_ambiente)) {
            return
        }
        if (!validEditText(etArea, tilArea, R.string.error_area)) {
            return
        }

        val id = etId.text.toString().trim()
        val place = etLocal.text.toString().trim()
        val ubicacion = etUbicacion.text.toString().trim()
        val responsible = spinnerResponsible.text.toString().trim()
        val position = etCargo.text.toString().trim()
        val office = etOficina.text.toString().trim()
        val ambient = etAmbiente.text.toString().trim()
        val area = etArea.text.toString().trim()
        val pending = checkPending.isChecked
        val obs = etObservation.text.toString().trim()


        // If we have received an ID, we have to edit the data, else we have to create a new record
        if (sheetId.isEmpty()) {
            val inventariador = Global.getFromSharedPreferences(activity, "username")
            val call = MyApiAdapter.getApiService().storeSheet(
                    id, place, ubicacion, responsible, position, office,
                    ambient, area, pending, obs, inventariador
            )
            call.enqueue(CreateSheetCallback())
        } else {
            val call = MyApiAdapter.getApiService().updateSheet(
                    id, place, ubicacion, responsible, position, office,
                    ambient, area, pending, obs
            )
            call.enqueue(EditSheetCallback())
        }
    }

    internal inner class CreateSheetCallback : Callback<SimpleResponse?> {
        override fun onResponse(call: Call<SimpleResponse?>, response: Response<SimpleResponse?>) {
            if (response.isSuccessful) {
                val simpleResponse = response.body()
                if (simpleResponse?.isError == true) {
                    context?.toast(simpleResponse.message)
                } else {
                    context?.toast(getString(R.string.success_sheet_created_message))

                    // Re-load the sheets
                    (activity as PanelActivity?)?.loadInventorySheets()
                    dismiss()
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<SimpleResponse?>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }
    }

    internal inner class EditSheetCallback : Callback<SimpleResponse?> {
        override fun onResponse(call: Call<SimpleResponse?>, response: Response<SimpleResponse?>) {
            if (response.isSuccessful) {
                val simpleResponse = response.body()
                if (simpleResponse!!.isError) {
                    context?.toast(simpleResponse.message)
                } else {
                    context?.toast(getString(R.string.success_sheet_updated_message))
                    // Re-load the sheets
                    (activity as PanelActivity?)?.loadInventorySheets()
                    dismiss()
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<SimpleResponse?>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }
    }

    private fun validEditText(editText: EditText, textInputLayout: TextInputLayout, errorString: Int): Boolean {
        if (editText.text.toString().trim().isEmpty()) {
            textInputLayout.error = getString(errorString)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun fetchHeaderDataFromServer() {
        val call = MyApiAdapter.getApiService().getSheet(sheetId)
        call.enqueue(ShowHeaderDataCallback())
    }

    internal inner class ShowHeaderDataCallback : Callback<HojaResponse?> {
        override fun onResponse(call: Call<HojaResponse?>, response: Response<HojaResponse?>) {
            if (response.isSuccessful) {
                val hojaResponse = response.body()
                if (hojaResponse!!.isError) {
                    context?.toast(hojaResponse.message)
                } else {
                    showHeaderDataInFields(hojaResponse.sheet)
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<HojaResponse?>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }

        private fun showHeaderDataInFields(sheet: Sheet) {
            etLocal.setText(sheet.place)
            etUbicacion.setText(sheet.location)
            etCargo.setText(sheet.position)
            etOficina.setText(sheet.office)
            etAmbiente.setText(sheet.ambient)
            etArea.setText(sheet.area)
            spinnerResponsible.setText(sheet.responsible_user)

            if (sheet.pending) {
                checkPending.isChecked = true
                // if pending show the observation field
                tilObservation.visibility = View.VISIBLE
                etObservation.setText(sheet.observation)
            }

            setCheckPendingOnChangeListener()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(sheetId: String?): HeaderDialogFragment {
            val f = HeaderDialogFragment()
            val args = Bundle()
            args.putString("hoja_id", sheetId)
            f.arguments = args
            return f
        }
    }
}