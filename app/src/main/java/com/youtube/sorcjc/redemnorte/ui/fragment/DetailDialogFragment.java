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
import android.util.Log;
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

import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;
import com.youtube.sorcjc.redemnorte.ui.DetailsActivity;
import com.youtube.sorcjc.redemnorte.ui.SimpleScannerActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDialogFragment extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText etQR, etPatrimonial, etOldCode,
            etDescription, etColor, etBrand, etModel, etSeries,
            etDimLong, etDimWidth, etDimHigh,
            etObservation;

    private Spinner spinnerPreservation, spinnerOldYear;
    private CheckBox checkOperative, checkSurplus;

    private TextInputLayout tilQR, tilPatrimonial,
            tilDescription, tilColor, tilBrand, tilModel, tilSeries,
            tilDimLong, tilDimWidth, tilDimHigh,
            tilObservation;

    private String hoja_id;

    public static DetailDialogFragment newInstance(String hoja_id) {
        DetailDialogFragment f = new DetailDialogFragment();

        Bundle args = new Bundle();
        args.putString("hoja_id", hoja_id);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_detail, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Registrar nuevo bien");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

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
        checkSurplus = (CheckBox) view.findViewById(R.id.checkSurplus);
        checkSurplus.setOnCheckedChangeListener(this);

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

        ImageButton btnCaptureQR = (ImageButton) view.findViewById(R.id.btnCaptureQR);
        btnCaptureQR.setOnClickListener(this);
        ImageButton btnCapturePatrimonial = (ImageButton) view.findViewById(R.id.btnCapturePatrimonial);
        btnCapturePatrimonial.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hoja_id = getArguments().getString("hoja_id");
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

        if (etPatrimonial.getText().toString().trim().isEmpty() && !checkSurplus.isChecked()) {
            tilPatrimonial.setError("Escanea el código o declara que es un bien sobrante !");
            return;
        } else {
            tilPatrimonial.setErrorEnabled(false);
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
        final String operativo = checkOperative.isChecked() ? "S" : "N";
        final String observacion = etObservation.getText().toString().trim();

        Call<SimpleResponse> call = RedemnorteApiAdapter.getApiService().postRegistrarBien(
                hoja_id, QR_code, patrimonial_code, old_code, old_year,
                denominacion, marca, modelo, serie, color,
                largo, ancho, alto,
                condicion, operativo, observacion
        );
        call.enqueue(new RegistrarBienCallback());
    }

    class RegistrarBienCallback implements Callback<SimpleResponse> {

        @Override
        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
            if (response.isSuccessful()) {
                if (response.body().isError()) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Se ha registrado un nuevo bien", Toast.LENGTH_SHORT).show();

                    // Re-load the recyclerView
                    ((DetailsActivity) getActivity()).cargarBienes();
                    // and then dismiss this dialog
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
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        // checkSurplus
        if (b) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Importante");
            alertDialog.setMessage("Un bien sobrante es aquel que no tiene código patrimonial asignado. Si no estás seguro, comunícate con tu superior.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }
}