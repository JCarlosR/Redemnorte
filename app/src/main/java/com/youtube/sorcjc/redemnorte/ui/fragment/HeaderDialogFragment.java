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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HeaderDialogFragment extends DialogFragment {

    private Spinner spinnerResponsible;
    private EditText etLocal, etUbicacion, etCargo, etDependencia, etAmbiente, etArea;
    private TextInputLayout tilLocal, tilUbicacion, tilCargo, tilDependencia, tilAmbiente, tilArea;

    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment


        View view = inflater.inflate(R.layout.dialog_new_header, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Registrar nueva hoja");
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

        spinnerResponsible = (Spinner) view.findViewById(R.id.spinnerResponsible);
        List<String> list = new ArrayList<String>();
        list.add("Usuario A");
        list.add("Usuario B");
        list.add("Usuario C");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResponsible.setAdapter(spinnerArrayAdapter);

        return view;
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
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