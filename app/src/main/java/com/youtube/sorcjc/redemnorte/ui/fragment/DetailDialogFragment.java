package com.youtube.sorcjc.redemnorte.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.Global;
import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter;
import com.youtube.sorcjc.redemnorte.io.response.BienResponse;
import com.youtube.sorcjc.redemnorte.io.response.ByOldCodeResponse;
import com.youtube.sorcjc.redemnorte.io.response.ByPatrimonialResponse;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;
import com.youtube.sorcjc.redemnorte.model.Bien;
import com.youtube.sorcjc.redemnorte.model.BienConsolidado;
import com.youtube.sorcjc.redemnorte.ui.DetailsActivity;
import com.youtube.sorcjc.redemnorte.ui.SimpleScannerActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText etQR, etPatrimonial, etOldCode,
            etDescription, etColor, etBrand, etModel, etSeries,
            etDimLong, etDimWidth, etDimHigh,
            etObservation;

    private Spinner spinnerPreservation, spinnerOldYear;
    private CheckBox checkOperative, checkEtiquetado;

    private TextInputLayout tilQR, tilPatrimonial,
            tilDescription, tilColor, tilBrand, tilModel, tilSeries,
            tilDimLong, tilDimWidth, tilDimHigh,
            tilObservation;

    private ImageButton btnCaptureQR, btnCheckQR;

    // Param that contains the ID of the parent header
    private String hoja_id;

    // The next param only is provided when the fragment is opened in edit mode
    private String qr_code_param;

    // Responsible associated with the header, so we can check if the detail is assigned to it
    // in old databases
    private String responsable;

    public static DetailDialogFragment newInstance(String hoja_id, String qr_code, String responsable) {
        DetailDialogFragment f = new DetailDialogFragment();

        Bundle args = new Bundle();
        args.putString("hoja_id", hoja_id);
        args.putString("qr_code", qr_code);
        args.putString("responsable", responsable);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hoja_id = getArguments().getString("hoja_id");
        qr_code_param = getArguments().getString("qr_code");
        responsable = getArguments().getString("responsable");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_detail, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        String title = "Registrar nuevo bien";
        if (qr_code_param!=null && !qr_code_param.isEmpty())
            title = "Editar bien seleccionado";
        toolbar.setTitle(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        getViewReferences(view);

        // Capture QR, Patrimonial Barcode & Old code
        btnCaptureQR = (ImageButton) view.findViewById(R.id.btnCaptureQR);
        btnCaptureQR.setOnClickListener(this);
        ImageButton btnCapturePatrimonial = (ImageButton) view.findViewById(R.id.btnCapturePatrimonial);
        btnCapturePatrimonial.setOnClickListener(this);
        ImageButton btnCaptureOldCode = (ImageButton) view.findViewById(R.id.btnCaptureOldCode);
        btnCaptureOldCode.setOnClickListener(this);

        // Check if QR is available
        btnCheckQR = (ImageButton) view.findViewById(R.id.btnCheckQR);
        btnCheckQR.setOnClickListener(this);

        // Take data by patrimonial code
        ImageButton btnTakeByPatrimonial = (ImageButton) view.findViewById(R.id.btnTakeByPatrimonial);
        btnTakeByPatrimonial.setOnClickListener(this);
        // Take data by old code
        ImageButton btnTakeByOldCode = (ImageButton) view.findViewById(R.id.btnTakeByOldCode);
        btnTakeByOldCode.setOnClickListener(this);

        setupEditMode();

        return view;
    }

    private void getViewReferences(View view) {
        etQR = (EditText) view.findViewById(R.id.etQR);
        etPatrimonial = (EditText) view.findViewById(R.id.etPatrimonial);

        etOldCode = (EditText) view.findViewById(R.id.etOldCode);
        spinnerOldYear = (Spinner) view.findViewById(R.id.spinnerOldYear);

        etDescription = (EditText) view.findViewById(R.id.etDescription);
        etColor = (EditText) view.findViewById(R.id.etColor);
        etBrand = (EditText) view.findViewById(R.id.etBrand);
        etModel = (EditText) view.findViewById(R.id.etModel);
        etSeries = (EditText) view.findViewById(R.id.etSeries);
        etDimLong = (EditText) view.findViewById(R.id.etDimLong);
        etDimWidth = (EditText) view.findViewById(R.id.etDimWidth);
        etDimHigh = (EditText) view.findViewById(R.id.etDimHigh);
        etObservation = (EditText) view.findViewById(R.id.etObservation);

        spinnerPreservation = (Spinner) view.findViewById(R.id.spinnerPreservation);
        checkOperative = (CheckBox) view.findViewById(R.id.checkOperative);
        checkEtiquetado = (CheckBox) view.findViewById(R.id.checkEtiquetado);

        tilQR = (TextInputLayout) view.findViewById(R.id.tilQR);
        tilPatrimonial = (TextInputLayout) view.findViewById(R.id.tilPatrimonial);
        tilDescription = (TextInputLayout) view.findViewById(R.id.tilDescription);
        tilColor = (TextInputLayout) view.findViewById(R.id.tilColor);
        tilBrand = (TextInputLayout) view.findViewById(R.id.tilBrand);
        tilModel = (TextInputLayout) view.findViewById(R.id.tilModel);
        tilSeries = (TextInputLayout) view.findViewById(R.id.tilSeries);
        tilDimLong = (TextInputLayout) view.findViewById(R.id.tilDimLong);
        tilDimWidth = (TextInputLayout) view.findViewById(R.id.tilDimWidth);
        tilDimHigh = (TextInputLayout) view.findViewById(R.id.tilDimHigh);
        tilObservation = (TextInputLayout) view.findViewById(R.id.tilObservation);
    }

    private void setupEditMode() {
        // qr_code_param provided => edit mode
        if (! qr_code_param.isEmpty()) {
            etQR.setEnabled(false);
            btnCaptureQR.setVisibility(View.GONE);
            btnCheckQR.setVisibility(View.GONE);

            Call<BienResponse> call = RedemnorteApiAdapter.getApiService().getBien(hoja_id, qr_code_param);
            call.enqueue(new GetPreviousDataCallback());
        }
    }

    class GetPreviousDataCallback implements Callback<BienResponse> {

        @Override
        public void onResponse(Call<BienResponse> call, Response<BienResponse> response) {
            if (response.isSuccessful()) {
                Bien bien = response.body().getBien();
                setProductDataToViews(bien);
            } else {
                Toast.makeText(getContext(), "Formato de respuesta incorrecto", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<BienResponse> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setProductDataToViews(Bien bien) {
        etQR.setText(bien.getQr());
        etPatrimonial.setText(bien.getPatrimonial());
        etOldCode.setText(bien.getOld_code());

        spinnerOldYear.setSelection(Global.getSpinnerIndex(spinnerOldYear, bien.getOld_year()));
        spinnerPreservation.setSelection(Global.getSpinnerIndex(spinnerPreservation, bien.getPreservation()));

        checkOperative.setChecked( bien.isOperative().equals("S") );
        checkEtiquetado.setChecked( bien.getEtiquetado().equals("1") );

        etDescription.setText(bien.getDescription());
        etColor.setText(bien.getColor());
        etBrand.setText(bien.getBrand());
        etModel.setText(bien.getModel());
        etSeries.setText(bien.getSeries());
        etDimLong.setText(bien.getDimLong());
        etDimWidth.setText(bien.getDimWidth());
        etDimHigh.setText(bien.getDimHigh());
        etObservation.setText(bien.getObservation());
    }

    @Override
    @NonNull
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
        if (!validateEditText(etQR, tilQR, R.string.error_til_qr)) {
            return;
        }

        if (!validateEditText(etDescription, tilDescription, R.string.error_til_description)) {
            return;
        }

        performRegisterRequest();
    }

    private void performRegisterRequest() {
        final String QR_code = etQR.getText().toString().trim();
        final String patrimonial_code = etPatrimonial.getText().toString().trim();
        final String old_code = etOldCode.getText().toString().trim();
        final String old_year = spinnerOldYear.getSelectedItem().toString();

        final String denominacion = etDescription.getText().toString().trim();
        final String marca = etBrand.getText().toString().trim();
        final String modelo = etModel.getText().toString().trim();
        final String serie = etSeries.getText().toString().trim();
        final String color = etColor.getText().toString().trim();

        final String largo = etDimLong.getText().toString().trim();
        final String ancho = etDimWidth.getText().toString().trim();
        final String alto = etDimHigh.getText().toString().trim();

        final String condicion = spinnerPreservation.getSelectedItem().toString();
        final String etiquetado = checkEtiquetado.isChecked() ? "1" : "0";
        final String operativo = checkOperative.isChecked() ? "S" : "N";
        final String observacion = etObservation.getText().toString().trim();

        Call<SimpleResponse> call;
        if (qr_code_param!=null && !qr_code_param.isEmpty()) {
            // Qr code provided => edit mode
             call = RedemnorteApiAdapter.getApiService().postEditarBien(
                    hoja_id, QR_code, patrimonial_code, old_code, old_year,
                    denominacion, marca, modelo, serie, color,
                    largo, ancho, alto,
                    condicion, etiquetado, operativo, observacion
            );
        } else {
            // Qr code assigned => register new detail
            call = RedemnorteApiAdapter.getApiService().postRegistrarBien(
                    hoja_id, QR_code, patrimonial_code, old_code, old_year,
                    denominacion, marca, modelo, serie, color,
                    largo, ancho, alto,
                    condicion, etiquetado, operativo, observacion
            );
        }
        call.enqueue(new RegistrarBienCallback());
    }

    class RegistrarBienCallback implements Callback<SimpleResponse> {

        @Override
        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
            if (response.isSuccessful()) {
                if (response.body().isError()) {
                    // Just show an error message.
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // Show a successful message,
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    // re-load the recyclerView,
                    ((DetailsActivity) getActivity()).cargarBienes();
                    // and dismiss this dialog.
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCaptureQR:
                Intent intentQR = new Intent(getContext(), SimpleScannerActivity.class);
                startActivityForResult(intentQR, 1);
                break;
            case R.id.btnCapturePatrimonial:
                Intent intentPatrimonial = new Intent(getContext(), SimpleScannerActivity.class);
                startActivityForResult(intentPatrimonial, 2);
                break;
            case R.id.btnCaptureOldCode:
                Intent intentCaptureOld = new Intent(getContext(), SimpleScannerActivity.class);
                startActivityForResult(intentCaptureOld, 3);
                break;

            case R.id.btnCheckQR:
                performCheckQrRequest();
                break;

            case R.id.btnTakeByPatrimonial:
                performByPatrimonialRequest();
                break;
            case R.id.btnTakeByOldCode:
                performByOldCodeRequest();
        }
    }

    private void performByPatrimonialRequest() {
        Call<ByPatrimonialResponse> call = RedemnorteApiAdapter.getApiService().getByPatrimonial(etPatrimonial.getText().toString().trim());
        call.enqueue(new TakeByPatrimonialCallback());
    }

    class TakeByPatrimonialCallback implements Callback<ByPatrimonialResponse> {
        @Override
        public void onResponse(Call<ByPatrimonialResponse> call, Response<ByPatrimonialResponse> response) {
            if (response.isSuccessful()) {
                ByPatrimonialResponse byPatrimonialResponse = response.body();
                // The message is used for both, successful and error responses
                if (byPatrimonialResponse.isError()) {
                    Toast.makeText(getContext(), byPatrimonialResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    setBienConsolidadoInViews(byPatrimonialResponse.getBienConsolidado());
                    Toast.makeText(getContext(), byPatrimonialResponse.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }

        @Override
        public void onFailure(Call<ByPatrimonialResponse> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setBienConsolidadoInViews(BienConsolidado bienConsolidado) {
        if (bienConsolidado == null) return;

        final String description = bienConsolidado.getDescription().trim();
        final String brand = bienConsolidado.getBrand().trim();
        final String model = bienConsolidado.getModel().trim();
        final String series = bienConsolidado.getSeries().trim();
        final String estado = bienConsolidado.getEstado();
        final String situacion = bienConsolidado.getSituacion().trim();
        // final String ubicacion = bienConsolidado.getUbicacion();
        // final String local = bienConsolidado.getLocal();

        // WHERE situacion =  'BP' OR situacion =  'BA' OR situacion =  'NO' OR situacion =  'NU'
        if (situacion.equals("BP") || situacion.equals("BA") || situacion.equals("NO") || situacion.equals("NU")) {
            final String title = "Importante";
            final String message = "Lamentablemente este bien ha sido dado de baja.";
            Global.showInformationDialog(getContext(), title, message);
            return;
        }

        etDescription.setText(description);
        etBrand.setText(brand);
        etModel.setText(model);
        etSeries.setText(series);

        String preservation = "";
        switch (estado) {
            case "BU":
                preservation = "Bueno";
                break;
            case "SE": // Sin estado
                preservation = "Bueno";
                break;
            case "RE":
                preservation = "Regular";
                break;
            case "MA":
                preservation = "Malo";
                break;
            case "PB": // Para baja
                preservation = "Malo";
                break;
            case "ER": // En reparación
                preservation = "Malo";
                break;
            case "IN":
                preservation = "Malo";
                break;
        }
        spinnerPreservation.setSelection(Global.getSpinnerIndex(spinnerPreservation, preservation));

        final String empleado = bienConsolidado.getEmpleado().trim();
        if (! empleado.equals(responsable)) {
            String message = "Este bien le pertenece al usuario " + empleado + ".\n";
            message += "Verifica si el responsable de esta hoja " + responsable + " se hará cargo.\n";
            message += "De caso contrario, crea una nueva hoja, márcala con el estado pendiente y registra allí el bien.";
            Global.showInformationDialog(getContext(), "Importante", message);
        }
    }

    private void performByOldCodeRequest() {
        final String year = spinnerOldYear.getSelectedItem().toString();
        final String code = etOldCode.getText().toString().trim();
        Call<ByOldCodeResponse> call = RedemnorteApiAdapter.getApiService().getByOldCode(year, code);
        call.enqueue(new TakeByOldCodeCallback());
    }

    class TakeByOldCodeCallback implements Callback<ByOldCodeResponse> {
        @Override
        public void onResponse(Call<ByOldCodeResponse> call, Response<ByOldCodeResponse> response) {
            if (response.isSuccessful()) {
                ByOldCodeResponse byOldCodeResponse = response.body();
                // The message is used for both, successful and error responses
                if (byOldCodeResponse.isError()) {
                    Toast.makeText(getContext(), byOldCodeResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    etDescription.setText(byOldCodeResponse.getBien().getDescription());
                    etPatrimonial.setText(byOldCodeResponse.getBien().getCodigoActivo());
                    Toast.makeText(getContext(), byOldCodeResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ByOldCodeResponse> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void performCheckQrRequest() {
        Call<SimpleResponse> call = RedemnorteApiAdapter.getApiService().getCheckQr(etQR.getText().toString().trim());
        call.enqueue(new CheckRequestCallback());
    }

    class CheckRequestCallback implements Callback<SimpleResponse> {

        @Override
        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<SimpleResponse> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                final String result = data.getStringExtra("code");
                etQR.setText(result);
                Toast.makeText(getContext(), "Usa el botón del ojito para verificar que no se repita el QR.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                final String result = data.getStringExtra("code");
                etPatrimonial.setText(result);
                Toast.makeText(getContext(), "Usa el botón de la diana para buscar y traer datos.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                final String result = data.getStringExtra("code");
                etOldCode.setText(result);
                Toast.makeText(getContext(), "Usa el botón de la diana para buscar y traer datos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}