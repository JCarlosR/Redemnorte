package com.youtube.sorcjc.redemnorte.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.io.response.PublicDataResponse
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.ui.activity.PanelActivity
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper.get
import com.youtube.sorcjc.redemnorte.util.arrayAdapter
import com.youtube.sorcjc.redemnorte.util.showInfoDialog
import com.youtube.sorcjc.redemnorte.util.toast
import kotlinx.android.synthetic.main.dialog_new_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeaderDialogFragment : DialogFragment() {

    private var sheetId: Int = -1

    private val preferences by lazy {
        context?.let { PreferenceHelper.defaultPrefs(it) }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dialog_new_header, container, false)

        arguments?.getInt("hoja_id")?.let {
            sheetId = it
        }

        fetchPublicData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title: String
        if (sheetId == -1) {
            title = getString(R.string.title_sheet_create)
            etId.visibility = View.GONE
        } else {
            title = getString(R.string.title_sheet_edit)
            fetchSheetFromServer()
            etId.setText(sheetId.toString())
            etId.isEnabled = false
        }

        toolbar.title = title

        val appCompatActivity = (activity as AppCompatActivity)
        appCompatActivity.setSupportActionBar(toolbar)

        appCompatActivity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }

        // set for new headers (for edit mode will be set later)
        if (sheetId == -1) {
            setCheckPendingOnChangeListener()
        }
    }

    private fun setCheckPendingOnChangeListener() {
        checkPending.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tilObservation.visibility = View.VISIBLE
                context?.showInfoDialog(getString(R.string.dialog_title_observation), getString(R.string.dialog_message_observation))
            } else {
                tilObservation.visibility = View.GONE
                etObservation.setText("")
            }
        }
    }

    private fun populateSpinners(publicDataResponse: PublicDataResponse) {
        context?.let {
            // val arrayAdapter = ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, publicDataResponse.responsibleUsers)
            atvResponsible.setAdapter(it.arrayAdapter(publicDataResponse.responsibleUsers))
            atvArea.setAdapter(it.arrayAdapter(publicDataResponse.areas))
            atvPlace.setAdapter(it.arrayAdapter(publicDataResponse.places))
        }

        /*spinnerResponsible.onItemClickListener = OnItemClickListener { adapterView, _, position, _ ->
            val responsible = adapterView.getItemAtPosition(position) as ResponsibleUser
            Log.i("SelectedText", responsible.name)
        }*/
    }

    private fun fetchPublicData() {
        val call = MyApiAdapter.getApiService().getPublicData()
        call.enqueue(PublicDataCallback())
    }

    internal inner class PublicDataCallback : Callback<PublicDataResponse> {
        override fun onResponse(call: Call<PublicDataResponse>, response: Response<PublicDataResponse>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    populateSpinners(it)
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<PublicDataResponse>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.let {
            super.onPrepareOptionsMenu(it)
            it.removeItem(R.id.search)
        }
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
        if (etId.visibility == View.VISIBLE && !validEditText(etId, tilId, R.string.error_hoja_id)) {
            return
        }
        if (!validEditText(atvPlace, tilPlace, R.string.error_local)) {
            return
        }
        if (!validEditText(etLocation, tilLocation, R.string.error_ubicacion)) {
            return
        }
        if (!validEditText(etPosition, tilPosition, R.string.error_cargo)) {
            return
        }
        if (!validEditText(etOffice, tilOffice, R.string.error_oficina)) {
            return
        }
        if (!validEditText(etAmbient, tilAmbient, R.string.error_ambiente)) {
            return
        }
        if (!validEditText(atvArea, tilArea, R.string.error_area)) {
            return
        }

        val id = etId.text.toString().trim().toInt()
        val place = atvPlace.text.toString().trim()
        val location = etLocation.text.toString().trim()
        val responsible = atvResponsible.text.toString().trim()
        val position = etPosition.text.toString().trim()
        val office = etOffice.text.toString().trim()
        val ambient = etAmbient.text.toString().trim()
        val area = atvArea.text.toString().trim()
        val pending = checkPending.isChecked
        val obs = etObservation.text.toString().trim()

        // If we have received an ID, we have to edit the data, else we have to create a new record
        if (sheetId == -1) {
            val author = preferences?.get("user_id", -1) ?: -1
            val call = MyApiAdapter.getApiService().storeSheet(
                    place, location, responsible, position, office,
                    ambient, area, pending, obs, author
            )
            call.enqueue(CreateSheetCallback())
        } else {
            val call = MyApiAdapter.getApiService().updateSheet(
                    id, place, location, responsible, position, office,
                    ambient, area, pending, obs
            )
            call.enqueue(EditSheetCallback())
        }
    }

    internal inner class CreateSheetCallback : Callback<Sheet> {
        override fun onResponse(call: Call<Sheet>, response: Response<Sheet>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    context?.toast(getString(R.string.success_sheet_created_message))

                    // Re-load the sheets
                    (activity as PanelActivity?)?.loadInventorySheets()
                    dismiss()
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<Sheet>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }
    }

    internal inner class EditSheetCallback : Callback<Sheet> {
        override fun onResponse(call: Call<Sheet>, response: Response<Sheet>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    context?.toast(getString(R.string.success_sheet_updated_message))

                    // Re-load the sheets
                    (activity as PanelActivity?)?.loadInventorySheets()
                    dismiss()
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<Sheet>, t: Throwable) {
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

    private fun fetchSheetFromServer() {
        val call = MyApiAdapter.getApiService().getSheet(sheetId)
        call.enqueue(ShowHeaderDataCallback())
    }

    internal inner class ShowHeaderDataCallback : Callback<Sheet> {
        override fun onResponse(call: Call<Sheet>, response: Response<Sheet>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    showHeaderDataInFields(it)
                }
            } else {
                context?.toast(getString(R.string.error_format_server_response))
            }
        }

        override fun onFailure(call: Call<Sheet>, t: Throwable) {
            context?.toast(t.localizedMessage)
        }

        private fun showHeaderDataInFields(sheet: Sheet) {
            atvPlace.setText(sheet.place)
            etLocation.setText(sheet.location)
            etPosition.setText(sheet.position)
            etOffice.setText(sheet.office)
            etAmbient.setText(sheet.ambient)
            atvArea.setText(sheet.area)
            atvResponsible.setText(sheet.responsible_user)

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
        fun newInstance(sheetId: Int): HeaderDialogFragment {
            val args = Bundle()
            args.putInt("hoja_id", sheetId)

            val f = HeaderDialogFragment()
            f.arguments = args

            return f
        }
    }
}