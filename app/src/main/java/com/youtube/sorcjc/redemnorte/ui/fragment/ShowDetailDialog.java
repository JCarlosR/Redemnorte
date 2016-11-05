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

import com.youtube.sorcjc.redemnorte.Global;
import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter;
import com.youtube.sorcjc.redemnorte.io.response.BienResponse;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;
import com.youtube.sorcjc.redemnorte.model.Bien;
import com.youtube.sorcjc.redemnorte.ui.DetailsActivity;
import com.youtube.sorcjc.redemnorte.ui.SimpleScannerActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailDialog extends DialogFragment implements Callback<BienResponse> {

    private EditText etQR, etPatrimonial, etOldCode,
            etDescription, etColor, etBrand, etModel, etSeries,
            etDimLong, etDimWidth, etDimHigh,
            etObservation, etPreservation;

    private Spinner spinnerOldYear;
    private CheckBox checkOperative, checkSurplus;

    // Params required for the request
    private String hoja_id, qr_code;

    public static ShowDetailDialog newInstance(String hoja_id, String qr_code) {
        ShowDetailDialog f = new ShowDetailDialog();

        Bundle args = new Bundle();
        args.putString("hoja_id", hoja_id);
        args.putString("qr_code", qr_code);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view_detail, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Datos del bien seleccionado");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            // actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
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

        etPreservation = (EditText) view.findViewById(R.id.etPreservation);
        checkOperative = (CheckBox) view.findViewById(R.id.checkOperative);
        checkSurplus = (CheckBox) view.findViewById(R.id.checkSurplus);
        // checkSurplus.setOnCheckedChangeListener(this);

        getProductDataByQrCode();

        return view;
    }

    private void getProductDataByQrCode() {
        Call<BienResponse> call = RedemnorteApiAdapter.getApiService().getBien(hoja_id, qr_code);
        call.enqueue(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qr_code = getArguments().getString("qr_code");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setProductDataToViews(Bien bien) {
        etQR.setText(bien.getQr());
        etPatrimonial.setText(bien.getPatrimonial());
        etOldCode.setText(bien.getOld_code());

        spinnerOldYear.setSelection(Global.getSpinnerIndex(spinnerOldYear, bien.getOld_year()));
        etPreservation.setText(bien.getPreservation());

        checkOperative.setSelected( bien.isOperative().equals("S") );
        checkSurplus.setSelected( bien.getPatrimonial().trim().isEmpty() );

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