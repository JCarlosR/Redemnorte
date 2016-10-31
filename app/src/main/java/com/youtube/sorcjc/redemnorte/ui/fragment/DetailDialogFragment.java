package com.youtube.sorcjc.redemnorte.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.R;

public class DetailDialogFragment extends DialogFragment {

    private EditText etLocal, etUbicacion, etCargo, etDependencia, etAmbiente, etArea;
    private TextInputLayout tilLocal, tilUbicacion, tilCargo, tilDependencia, tilAmbiente, tilArea;

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

        etLocal = (EditText) view.findViewById(R.id.etLocal);
        etUbicacion = (EditText) view.findViewById(R.id.etUbicacion);
        etCargo = (EditText) view.findViewById(R.id.etCargo);
        etDependencia = (EditText) view.findViewById(R.id.etDependencia);
        etAmbiente = (EditText) view.findViewById(R.id.etAmbiente);
        etArea = (EditText) view.findViewById(R.id.etArea);

        tilLocal = (TextInputLayout) view.findViewById(R.id.tilLocal);
        tilUbicacion = (TextInputLayout) view.findViewById(R.id.tilUbicacion);
        tilCargo = (TextInputLayout) view.findViewById(R.id.tilCargo);
        tilDependencia = (TextInputLayout) view.findViewById(R.id.tilDependencia);
        tilAmbiente = (TextInputLayout) view.findViewById(R.id.tilAmbiente);
        tilArea = (TextInputLayout) view.findViewById(R.id.tilArea);

        return view;
    }

    /** The system calls this only when creating the layout in a dialog. */
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
        if (!validateEditText(etLocal, tilLocal, R.string.error_local)) {
            return;
        }

        if (!validateEditText(etUbicacion, tilUbicacion, R.string.error_ubicacion)) {
            return;
        }

        if (!validateEditText(etDependencia, tilDependencia, R.string.error_dependencia)) {
            return;
        }

        if (!validateEditText(etAmbiente, tilAmbiente, R.string.error_ambiente)) {
            return;
        }

        if (!validateEditText(etArea, tilArea, R.string.error_area)) {
            return;
        }

        Toast.makeText(getContext(), "Realizar petición", Toast.LENGTH_SHORT).show();
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

}