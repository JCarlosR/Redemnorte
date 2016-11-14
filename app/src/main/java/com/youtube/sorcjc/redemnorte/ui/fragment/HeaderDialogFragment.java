package com.youtube.sorcjc.redemnorte.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.Global;
import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter;
import com.youtube.sorcjc.redemnorte.io.response.HojaResponse;
import com.youtube.sorcjc.redemnorte.io.response.ResponsableResponse;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;
import com.youtube.sorcjc.redemnorte.model.Hoja;
import com.youtube.sorcjc.redemnorte.model.Responsable;
import com.youtube.sorcjc.redemnorte.ui.PanelActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeaderDialogFragment extends DialogFragment {

    private AutoCompleteTextView spinnerResponsible;
    private EditText etId, etLocal, etUbicacion, etCargo, etOficina, etAmbiente, etArea, etObservation;
    private TextInputLayout tilId, tilLocal, tilUbicacion, tilCargo, tilOficina, tilAmbiente, tilArea, tilObservation;
    private CheckBox checkPendiente;

    private String hoja_id;

    public static HeaderDialogFragment newInstance(String hoja_id) {
        HeaderDialogFragment f = new HeaderDialogFragment();

        Bundle args = new Bundle();
        args.putString("hoja_id", hoja_id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hoja_id = getArguments().getString("hoja_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_header, container, false);

        etId = (EditText) view.findViewById(R.id.etId);

        String title;
        if (hoja_id.isEmpty())
            title = "Registrar nueva hoja";
        else {
            title = "Editar hoja";
            fetchHeaderDataFromServer();
            etId.setText(hoja_id);
            etId.setEnabled(false);
        }

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        etLocal = (EditText) view.findViewById(R.id.etLocal);
        etUbicacion = (EditText) view.findViewById(R.id.etUbicacion);
        etCargo = (EditText) view.findViewById(R.id.etCargo);
        etOficina = (EditText) view.findViewById(R.id.etOficina);
        etAmbiente = (EditText) view.findViewById(R.id.etAmbiente);
        etArea = (EditText) view.findViewById(R.id.etArea);
        etObservation = (EditText) view.findViewById(R.id.etObservation);

        tilId = (TextInputLayout) view.findViewById(R.id.tilId);
        tilLocal = (TextInputLayout) view.findViewById(R.id.tilLocal);
        tilUbicacion = (TextInputLayout) view.findViewById(R.id.tilUbicacion);
        tilCargo = (TextInputLayout) view.findViewById(R.id.tilCargo);
        tilOficina = (TextInputLayout) view.findViewById(R.id.tilOficina);
        tilAmbiente = (TextInputLayout) view.findViewById(R.id.tilAmbiente);
        tilArea = (TextInputLayout) view.findViewById(R.id.tilArea);
        tilObservation = (TextInputLayout) view.findViewById(R.id.tilObservation);

        obtenerDatosResponsables();

        spinnerResponsible = (AutoCompleteTextView) view.findViewById(R.id.spinnerResponsible);
        checkPendiente = (CheckBox) view.findViewById(R.id.checkPendiente);
        if (hoja_id.isEmpty()) { // set for new headers (for edit mode will be set later)
            setCheckPendienteOnChangeListener();
        }

        return view;
    }

    private void setCheckPendienteOnChangeListener() {
        checkPendiente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tilObservation.setVisibility(View.VISIBLE);
                    Global.showInformationDialog(getContext(), "Observación", "¿Por qué motivo la hoja se ha marcado como pendiente?");
                } else {
                    tilObservation.setVisibility(View.GONE);
                    etObservation.setText("");
                }
            }
        });
    }

    private void poblarSpinnerResponsables(ArrayList<Responsable> responsables) {
        List<String> list = new ArrayList<String>();
        for (Responsable r : responsables) {
            list.add(r.getNombre());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, list);
        // spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResponsible.setAdapter(spinnerArrayAdapter);
    }

    private void obtenerDatosResponsables() {
        Call<ResponsableResponse> call = RedemnorteApiAdapter.getApiService().getResponsables();
        call.enqueue(new ResponsablesCallback());
    }

    class ResponsablesCallback implements Callback<ResponsableResponse> {

        @Override
        public void onResponse(Call<ResponsableResponse> call, Response<ResponsableResponse> response) {
            if (response.isSuccessful()) {
                ResponsableResponse responsableResponse = response.body();
                if (! responsableResponse.isError()) {
                    poblarSpinnerResponsables(responsableResponse.getResponsables());
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponsableResponse> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            validateForm();
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateForm() {
        if (!validateEditText(etId, tilId, R.string.error_hoja_id)) {
            return;
        }

        if (!validateEditText(etLocal, tilLocal, R.string.error_local)) {
            return;
        }

        if (!validateEditText(etUbicacion, tilUbicacion, R.string.error_ubicacion)) {
            return;
        }

        if (!validateEditText(etCargo, tilCargo, R.string.error_cargo)) {
            return;
        }

        if (!validateEditText(etOficina, tilOficina, R.string.error_oficina)) {
            return;
        }

        if (!validateEditText(etAmbiente, tilAmbiente, R.string.error_ambiente)) {
            return;
        }

        if (!validateEditText(etArea, tilArea, R.string.error_area)) {
            return;
        }

        final String id = etId.getText().toString().trim();
        final String local = etLocal.getText().toString().trim();
        final String ubicacion = etUbicacion.getText().toString().trim();
        final String responsable = spinnerResponsible.getText().toString().trim();
        final String cargo = etCargo.getText().toString().trim();
        final String oficina = etOficina.getText().toString().trim();
        final String ambiente = etAmbiente.getText().toString().trim();
        final String area = etArea.getText().toString().trim();
        final String activo = checkPendiente.isChecked() ? "0" : "1";
        final String observacion = etObservation.getText().toString().trim();

        // If we have received an ID, we have to edit the data, else, we have to create a new record
        if (hoja_id.isEmpty()) {
            final String inventariador = Global.getFromSharedPreferences(getActivity(), "username");

            Call<SimpleResponse> call = RedemnorteApiAdapter.getApiService().postRegistrarHoja(
                    id, local, ubicacion, responsable, cargo, oficina,
                    ambiente, area, activo, observacion, inventariador
            );
            call.enqueue(new RegistrarHojaCallback());
        } else {
            Call<SimpleResponse> call = RedemnorteApiAdapter.getApiService().postEditarHoja(
                    id, local, ubicacion, responsable, cargo, oficina,
                    ambiente, area, activo, observacion
            );
            call.enqueue(new EditarHojaCallback());
        }
    }

    class RegistrarHojaCallback implements Callback<SimpleResponse> {

        @Override
        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
            if (response.isSuccessful()) {
                SimpleResponse simpleResponse = response.body();
                if (simpleResponse.isError()) {
                    // Log.d("HeaderDialog", "messageError => " + simpleResponse.getMessage());
                    Toast.makeText(getContext(), simpleResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Se ha registrado una nueva hoja", Toast.LENGTH_SHORT).show();

                    // Re-load the sheets
                    ((PanelActivity) getActivity()).cargarHojas();
                    dismiss();
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<SimpleResponse> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class EditarHojaCallback implements Callback<SimpleResponse> {

        @Override
        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
            if (response.isSuccessful()) {
                SimpleResponse simpleResponse = response.body();
                if (simpleResponse.isError()) {
                    Toast.makeText(getContext(), simpleResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Se ha editado correctamente la hoja", Toast.LENGTH_SHORT).show();

                    // Re-load the sheets
                    ((PanelActivity) getActivity()).cargarHojas();
                    dismiss();
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<SimpleResponse> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateEditText(EditText editText, TextInputLayout textInputLayout, int errorString) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(getString(errorString));
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void fetchHeaderDataFromServer() {
        Call<HojaResponse> call = RedemnorteApiAdapter.getApiService().getHoja(hoja_id);
        call.enqueue(new ShowHeaderDataCallback());
    }

    class ShowHeaderDataCallback implements Callback<HojaResponse> {

        @Override
        public void onResponse(Call<HojaResponse> call, Response<HojaResponse> response) {
            if (response.isSuccessful()) {
                HojaResponse hojaResponse = response.body();
                if (hojaResponse.isError()) {
                    Toast.makeText(getContext(), hojaResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    showHeaderDataInFields(hojaResponse.getHoja());
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<HojaResponse> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        private void showHeaderDataInFields(Hoja hoja) {
            etLocal.setText(hoja.getLocal());
            etUbicacion.setText(hoja.getUbicacion());
            etCargo.setText(hoja.getCargo());
            etOficina.setText(hoja.getOficina());
            etAmbiente.setText(hoja.getAmbiente());
            etArea.setText(hoja.getArea());

            spinnerResponsible.setText(hoja.getResponsable());

            if ( hoja.getActivo().equals("0") ) {
                // active==0 => pendiente
                checkPendiente.setChecked(true);
                // pendiente => show observation field
                tilObservation.setVisibility(View.VISIBLE);
                etObservation.setText(hoja.getObservacion());
            }

            setCheckPendienteOnChangeListener();
        }
    }

}