package com.youtube.sorcjc.redemnorte.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.youtube.sorcjc.redemnorte.Global;
import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter;
import com.youtube.sorcjc.redemnorte.io.response.BienResponse;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;
import com.youtube.sorcjc.redemnorte.model.Bien;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailDialog extends DialogFragment implements Callback<BienResponse>, View.OnClickListener {

    private static final int REQUEST_CODE_CAMERA = 10101;
    private final String DEFAULT_PHOTO_EXTENSION = "jpg";

    // Location of the last photo taken
    private String currentPhotoPath;

    private EditText etQR, etPatrimonial, etOldCode,
            etDescription, etColor, etBrand, etModel, etSeries,
            etDimLong, etDimWidth, etDimHigh,
            etObservation, etPreservation;

    private Spinner spinnerOldYear;
    private CheckBox checkOperative, checkEtiquetado;

    // Params required for the request
    private String hoja_id, qr_code;

    // Photo
    Button btnCapturePhoto;
    private ImageView ivPhoto;

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
        checkEtiquetado = (CheckBox) view.findViewById(R.id.checkEtiquetado);

        btnCapturePhoto = (Button) view.findViewById(R.id.btnCapturePhoto);
        btnCapturePhoto.setOnClickListener(this);

        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);

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

        checkOperative.setChecked( bien.isOperative().equals("S") );
        checkEtiquetado.setChecked( bien.getEtiquetado().trim().equals("1") );

        etDescription.setText(bien.getDescription());
        etColor.setText(bien.getColor());
        etBrand.setText(bien.getBrand());
        etModel.setText(bien.getModel());
        etSeries.setText(bien.getSeries());
        etDimLong.setText(bien.getDimLong());
        etDimWidth.setText(bien.getDimWidth());
        etDimHigh.setText(bien.getDimHigh());
        etObservation.setText(bien.getObservation());

        final String extension = bien.getPhoto_extension();
        if (extension!=null && !extension.isEmpty()) {
            loadDetailPhoto(extension);
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCapturePhoto:
                capturePhoto();
                break;
        }
    }

    private void capturePhoto() {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createDestinationFile();
        } catch (IOException ex) {
            return;
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
        }
    }

    private File createDestinationFile() throws IOException {
        // Path for the temporary image and its name
        final File storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        final String imageFileName = "" + System.currentTimeMillis();

        File image = File.createTempFile(
                imageFileName,          // prefix
                "." + DEFAULT_PHOTO_EXTENSION, // suffix
                storageDirectory              // directory
        );

        // Save a the file path
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        postPicture(bitmap, DEFAULT_PHOTO_EXTENSION);
        boolean deleted = new File(currentPhotoPath).delete();
        if (! deleted) {
            Toast.makeText(getContext(), "Si desea luego puede eliminar la foto del celular", Toast.LENGTH_SHORT).show();
        }
    }

    private void postPicture(Bitmap bitmap, final String extension) {
        final String base64 = Global.getBase64FromBitmap(bitmap);

        Call<SimpleResponse> call = RedemnorteApiAdapter.getApiService()
                .postPhoto(base64, extension, hoja_id, qr_code);

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "La foto se ha subido correctamente", Toast.LENGTH_SHORT).show();
                    loadDetailPhoto(DEFAULT_PHOTO_EXTENSION);
                } else {
                    Toast.makeText(getActivity(), "Ocurrió un problema al enviar la imagen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDetailPhoto(final String extension) {
        final String imageUrl = Global.getProductPhotoUrl(hoja_id, qr_code, extension);
        Picasso.with(getContext()).load(imageUrl).fit().centerCrop().into(ivPhoto);
        btnCapturePhoto.setText("Reemplazar foto");
    }
}